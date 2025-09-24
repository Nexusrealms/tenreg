package de.nexusrealms.tenreg.data.functions;

import de.nexusrealms.tenreg.item.ItemEntry;
import net.minecraft.client.data.*;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public interface ItemModeler {
    static <T extends Item> BiConsumer<ItemEntry<T>, ItemModelGenerator> blockCubeAll(Identifier id){
        return (itemEntry, modelGenerator) ->modelGenerator.output.accept(itemEntry.asItem(), ItemModels.basic(Models.CUBE_ALL.upload(itemEntry.asItem(), TextureMap.all(id), modelGenerator.modelCollector)));
    }
    static <T extends Item> BiConsumer<ItemEntry<T>, ItemModelGenerator> blockCubeAll(String suffix){
        return (itemEntry, itemModelGenerator) -> ItemModeler.<T>blockCubeAll(itemEntry.getId().withSuffixedPath(suffix).withPrefixedPath("block/")).accept(itemEntry, itemModelGenerator);
    }
}
