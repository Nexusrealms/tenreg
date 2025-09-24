package de.nexusrealms.tenreg.data.providers;

import de.nexusrealms.tenreg.data.interfaces.EntryWithBlockLoot;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TenregBlockLoot extends FabricBlockLootTableProvider {
    private final List<EntryWithBlockLoot> list;
    public TenregBlockLoot(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup, List<EntryWithBlockLoot> list) {
        super(dataOutput, registryLookup);
        this.list = list;
    }

    @Override
    public void generate() {
        list.forEach(entryWithBlockLoot -> entryWithBlockLoot.accept(this));
    }
}
