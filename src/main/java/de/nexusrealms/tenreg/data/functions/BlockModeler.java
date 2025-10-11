package de.nexusrealms.tenreg.data.functions;

import de.nexusrealms.tenreg.block.BlockEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.data.*;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.util.Identifier;
import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.function.BiConsumer;

import static net.minecraft.client.data.BlockStateModelGenerator.createSlabBlockState;
import static net.minecraft.client.data.BlockStateModelGenerator.createWeightedVariant;
@FunctionalInterface
public interface BlockModeler<T extends Block> {
    void generate(BlockEntry<T> blockEntry, BlockStateModelGenerator modelGenerator);
    static <T extends Block> BlockModeler<T> simpleCubeAll(){
        return switch (FabricLoader.getInstance().getEnvironmentType()) {
            case CLIENT -> (tBlockEntry, modelGenerator) -> modelGenerator.registerSimpleCubeAll(tBlockEntry.getValue());
            case SERVER -> null;
        };
    }
    static <T extends Block> BlockModeler<T> prefixedMultiSimpleCubeAll(String prefix, String... suffixes){
        return switch (FabricLoader.getInstance().getEnvironmentType()){
            case CLIENT -> (tBlockEntry, modelGenerator) -> {
                modelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(tBlockEntry.getValue(),
                        createWeightedVariant(Arrays.stream(suffixes)
                                .map(suffix -> BlockStateModelGenerator
                                        .createModelVariant(Models.CUBE_ALL.upload(ModelIds.getBlockSubModelId(tBlockEntry.getValue(), suffix),
                                                TextureMap.all(tBlockEntry.getId().withPrefixedPath(prefix).withSuffixedPath(suffix).withPrefixedPath("block/")), modelGenerator.modelCollector)))
                                .toArray(ModelVariant[]::new))));
            };
            case SERVER -> null;
        };
    }
    static <T extends Block> BlockModeler<T> multiSimpleCubeAll(String... suffixes){
        return prefixedMultiSimpleCubeAll("", suffixes);
    }
    static <T extends Block> BlockModeler<T> slab(Block block){
        return switch (FabricLoader.getInstance().getEnvironmentType()){
            case CLIENT -> (tBlockEntry, modelGenerator) -> {
                Block slab = tBlockEntry.get();
                TextureMap textureMap = TextureMap.all(block);
                TextureMap textureMap2 = TextureMap.sideEnd(TextureMap.getSubId(slab, "_side"), textureMap.getTexture(TextureKey.TOP));
                WeightedVariant weightedVariant = createWeightedVariant(Models.SLAB.upload(slab, textureMap2, modelGenerator.modelCollector));
                WeightedVariant weightedVariant2 = createWeightedVariant(Models.SLAB_TOP.upload(slab, textureMap2, modelGenerator.modelCollector));
                WeightedVariant weightedVariant3 = createWeightedVariant(Models.CUBE_COLUMN.uploadWithoutVariant(slab, "_double", textureMap2, modelGenerator.modelCollector));
                modelGenerator.blockStateCollector.accept(createSlabBlockState(slab, weightedVariant, weightedVariant2, weightedVariant3));
            };
            case SERVER -> null;
        };
    }
    static <T extends Block> BlockModeler<T> stairs(Block block){
        return switch (FabricLoader.getInstance().getEnvironmentType()){
            case CLIENT -> (tBlockEntry, modelGenerator) -> {
                Identifier oId = TextureMap.getId(block);
                Block stair = tBlockEntry.get();
                TextureMap stairs = new TextureMap()
                        .put(TextureKey.BOTTOM, oId)
                        .put(TextureKey.SIDE, oId)
                        .put(TextureKey.TOP, oId);
                WeightedVariant weightedVariant = BlockStateModelGenerator.createWeightedVariant(Models.INNER_STAIRS.upload(stair, stairs, modelGenerator.modelCollector));
                Identifier identifier = Models.STAIRS.upload(stair, stairs, modelGenerator.modelCollector);
                WeightedVariant weightedVariant2 = BlockStateModelGenerator.createWeightedVariant(Models.OUTER_STAIRS.upload(stair, stairs, modelGenerator.modelCollector));
                modelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createStairsBlockState(stair, weightedVariant, BlockStateModelGenerator.createWeightedVariant(identifier), weightedVariant2));
            };
            case SERVER -> null;
        };
    }
    static <T extends Block> BlockModeler<T> pillar(){
        return switch (FabricLoader.getInstance().getEnvironmentType()){
            case CLIENT -> (tBlockEntry, modelGenerator) -> modelGenerator.registerAxisRotated(tBlockEntry.get(), TexturedModel.CUBE_COLUMN);
            case SERVER -> null;
        };
    }
    static <T extends Block> BlockModeler<T> wall(Block block, boolean withItem){
        return switch (FabricLoader.getInstance().getEnvironmentType()){
            case CLIENT -> (tBlockEntry, modelGenerator) -> {
                Identifier oId = TextureMap.getId(block);
                Block wall = tBlockEntry.get();
                TextureMap wallTextures = new TextureMap()
                        .put(TextureKey.WALL, oId);
                WeightedVariant weightedVariant = BlockStateModelGenerator.createWeightedVariant(Models.TEMPLATE_WALL_POST.upload(wall, wallTextures, modelGenerator.modelCollector));
                WeightedVariant weightedVariant2 = BlockStateModelGenerator.createWeightedVariant(Models.TEMPLATE_WALL_SIDE.upload(wall, wallTextures, modelGenerator.modelCollector));
                WeightedVariant weightedVariant3 = BlockStateModelGenerator.createWeightedVariant(Models.TEMPLATE_WALL_SIDE_TALL.upload(wall, wallTextures, modelGenerator.modelCollector));
                modelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createWallBlockState(wall, weightedVariant, weightedVariant2, weightedVariant3));
                if(withItem){
                    Identifier identifier = Models.WALL_INVENTORY.upload(wall, wallTextures, modelGenerator.modelCollector);
                    modelGenerator.registerParentedItemModel(tBlockEntry.getValue(), identifier);
                }
            };
            case SERVER -> null;
        };
    }
    static <T extends Block> BlockModeler<T> button(Block block, boolean withItem){
        return switch (FabricLoader.getInstance().getEnvironmentType()){
            case CLIENT -> (tBlockEntry, modelGenerator) -> {
                Identifier oId = TextureMap.getId(block);
                Block button = tBlockEntry.get();
                TextureMap textures = new TextureMap()
                        .put(TextureKey.TEXTURE, oId);
                WeightedVariant weightedVariant = BlockStateModelGenerator.createWeightedVariant(Models.BUTTON.upload(button, textures, modelGenerator.modelCollector));
                WeightedVariant weightedVariant2 = BlockStateModelGenerator.createWeightedVariant(Models.BUTTON_PRESSED.upload(button, textures, modelGenerator.modelCollector));
                modelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createButtonBlockState(button, weightedVariant, weightedVariant2));
                if(withItem){
                    Identifier identifier = Models.BUTTON_INVENTORY.upload(button, textures, modelGenerator.modelCollector);
                    modelGenerator.registerParentedItemModel(button, identifier);
                }
            };
            case SERVER -> null;
        };
    }
    static <T extends Block> BlockModeler<T> pressurePlate(Block block){
        return switch (FabricLoader.getInstance().getEnvironmentType()){
            case CLIENT -> (tBlockEntry, modelGenerator) -> {
                Identifier oId = TextureMap.getId(block);
                Block pressurePlate = tBlockEntry.get();
                TextureMap textures = new TextureMap()
                        .put(TextureKey.TEXTURE, oId);
                WeightedVariant weightedVariant = BlockStateModelGenerator.createWeightedVariant(Models.PRESSURE_PLATE_UP.upload(pressurePlate, textures, modelGenerator.modelCollector));
                WeightedVariant weightedVariant2 = BlockStateModelGenerator.createWeightedVariant(Models.PRESSURE_PLATE_DOWN.upload(pressurePlate, textures, modelGenerator.modelCollector));
                modelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createPressurePlateBlockState(pressurePlate, weightedVariant, weightedVariant2));

            };
            case SERVER -> null;
        };
    }
}
