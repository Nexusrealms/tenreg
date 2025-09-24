package de.nexusrealms.tenreg.data.providers;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import de.nexusrealms.tenreg.item.ItemEntry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TenregItemTags extends FabricTagProvider.ItemTagProvider {
    public TenregItemTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, @Nullable BlockTagProvider blockTagProvider) {
        super(output, registriesFuture, blockTagProvider);
    }
    private final Multimap<TagKey<Item>, Item> map = HashMultimap.create();
    public void addTag(TagKey<Item> tagKey, Item item){
        map.put(tagKey, item);
    }
    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        map.keySet().forEach(tagKey -> valueLookupBuilder(tagKey).add(map.get(tagKey)));
    }
}
