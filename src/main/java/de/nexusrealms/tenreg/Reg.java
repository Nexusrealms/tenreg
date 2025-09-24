package de.nexusrealms.tenreg;

import de.nexusrealms.tenreg.block.BlockReg;
import de.nexusrealms.tenreg.data.interfaces.*;
import de.nexusrealms.tenreg.data.providers.*;
import de.nexusrealms.tenreg.item.ItemReg;
import lombok.Getter;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class Reg {
    @Getter
    private final String modId;
    @Getter
    private boolean frozen = false;
    private final Map<RegistryKey<? extends Registry<?>>, SubReg<?>> subRegs = new HashMap<>();
    //Items are a special case as this reg needs to be accessed by other regs
    @Getter
    private final ItemReg itemReg = new ItemReg(this);
    private Reg(String modId) {
        this.modId = modId;
        this.subRegs.put(RegistryKeys.ITEM, itemReg);
    }
    public BlockReg getBlockReg(){
        return (BlockReg) subRegs.computeIfAbsent(RegistryKeys.BLOCK, registryKey -> new BlockReg(this));
    }
    public static Reg create(String modId){
        return new Reg(modId);
    }
    public Identifier id(String path){
        return Identifier.of(modId, path);
    }
    public Function<String, Identifier> id(){
        return this::id;
    }
    public void register() {
        if (!frozen){
            subRegs.values().forEach(SubReg::register);
            frozen = true;
            subRegs.values().forEach(SubReg::postRegister);
        } else {
            throw new IllegalStateException("Root Reg already frozen");
        }
    }
    public Stream<RegEntry<?>> streamEntries(){
        return subRegs.values().stream().flatMap(SubReg::streamEntries);
    }
    public <A> Stream<A> streamMatchingEntriesAs(Class<A> clazz){
        return streamEntries().filter(entry -> clazz.isAssignableFrom(entry.getClass())).map(clazz::cast);
    }
    public void genData(FabricDataGenerator.Pack pack){
        TenregBlockTags blockTags = pack.addProvider(TenregBlockTags::new);
        streamMatchingEntriesAs(EntryWithBlockTag.class).forEach(entryWithBlockTag -> entryWithBlockTag.accept(blockTags));
        TenregItemTags itemTags = pack.addProvider((fabricDataOutput, completableFuture) -> new TenregItemTags(fabricDataOutput, completableFuture, blockTags));
        streamMatchingEntriesAs(EntryWithItemTag.class).forEach(entryWithItemTag -> entryWithItemTag.accept(itemTags));
        TenregEntityTags entityTags = pack.addProvider(TenregEntityTags::new);
        streamMatchingEntriesAs(EntryWithEntityTag.class).forEach(entryWithEntityTag -> entryWithEntityTag.accept(entityTags));
        TenregModels models = pack.addProvider(TenregModels::new);
        streamMatchingEntriesAs(EntryWithBlockModel.class).forEach(models::addBlock);
        streamMatchingEntriesAs(EntryWithItemModel.class).forEach(models::addItem);
        pack.addProvider((fabricDataOutput, completableFuture) -> new TenregRecipes(fabricDataOutput, completableFuture, streamMatchingEntriesAs(EntryWithRecipe.class).toList()));
        pack.addProvider((fabricDataOutput, completableFuture) -> new TenregLang(fabricDataOutput, completableFuture, streamMatchingEntriesAs(EntryWithTranslation.class).toList()));
        pack.addProvider((fabricDataOutput, completableFuture) -> new TenregBlockLoot(fabricDataOutput, completableFuture, streamMatchingEntriesAs(EntryWithBlockLoot.class).toList()));

    }
}
