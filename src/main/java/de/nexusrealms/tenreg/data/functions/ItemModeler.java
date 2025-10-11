package de.nexusrealms.tenreg.data.functions;

import de.nexusrealms.tenreg.block.BlockEntry;
import de.nexusrealms.tenreg.item.ItemEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.data.*;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;
@FunctionalInterface
public interface ItemModeler<T extends Item> {

    void generate(ItemEntry<T> itemEntry, ItemModelGenerator modelGenerator);
    static <T extends Item> ItemModeler<T> block(BlockEntry<?> blockEntry){
        return switch (FabricLoader.getInstance().getEnvironmentType()){
            case CLIENT -> (itemItemEntry, itemModelGenerator) -> itemModelGenerator.output.accept(itemItemEntry.asItem(), ItemModels.basic(blockEntry.getKey().getValue().withPrefixedPath("block/")));
            case SERVER -> null;
        };
    }
    static <T extends Item> ItemModeler<T> simple(){
        return switch (FabricLoader.getInstance().getEnvironmentType()){
            case CLIENT -> (itemEntry, modelGenerator) -> modelGenerator.register(itemEntry.asItem(), Models.GENERATED);
            case SERVER -> null;
        };
    }
    static <T extends Item> ItemModeler<T> blockCubeAll(Identifier id){
        return switch (FabricLoader.getInstance().getEnvironmentType()){
            case CLIENT ->  (itemEntry, modelGenerator) ->modelGenerator.output.accept(itemEntry.asItem(), ItemModels.basic(Models.CUBE_ALL.upload(itemEntry.asItem(), TextureMap.all(id), modelGenerator.modelCollector)));
            case SERVER -> null;
        };
    }
    static <T extends Item> ItemModeler<T> blockCubeAll(String suffix){
        return switch (FabricLoader.getInstance().getEnvironmentType()){
            case CLIENT ->  (itemEntry, itemModelGenerator) -> ItemModeler.<T>blockCubeAll(itemEntry.getId().withSuffixedPath(suffix).withPrefixedPath("block/")).generate(itemEntry, itemModelGenerator);
            case SERVER -> null;
        };
    }
}
