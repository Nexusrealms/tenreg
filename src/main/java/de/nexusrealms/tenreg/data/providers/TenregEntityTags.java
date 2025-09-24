package de.nexusrealms.tenreg.data.providers;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;

import java.util.concurrent.CompletableFuture;

public class TenregEntityTags extends FabricTagProvider.EntityTypeTagProvider {

    private final Multimap<TagKey<EntityType<?>>, EntityType<?>> map = HashMultimap.create();

    public TenregEntityTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    public void addTag(TagKey<EntityType<?>> tagKey, EntityType<?> entityType){
        map.put(tagKey, entityType);
    }
    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        map.keySet().forEach(tagKey -> valueLookupBuilder(tagKey).add(map.get(tagKey)));
    }
}
