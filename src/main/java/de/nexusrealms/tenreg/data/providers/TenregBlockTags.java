package de.nexusrealms.tenreg.data.providers;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TenregBlockTags extends FabricTagProvider.BlockTagProvider {

    private final Multimap<TagKey<Block>, Block> map = HashMultimap.create();

    public TenregBlockTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    public void addTag(TagKey<Block> tagKey, Block block){
        map.put(tagKey, block);
    }
    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        map.keySet().forEach(tagKey -> valueLookupBuilder(tagKey).add(map.get(tagKey)));
    }
}
