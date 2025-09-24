package de.nexusrealms.tenreg.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

import java.util.function.Consumer;

public interface BlockTransformers {
    @SafeVarargs
    static <T extends Block> Consumer<BlockEntry.Builder<T>> mineableRequiriesTool(TagKey<Block>... blockTag){
        return tBuilder -> tBuilder.settingsUpdater(AbstractBlock.Settings::requiresTool)
                .tag(blockTag);
    }
    static  <T extends Block> Consumer<BlockEntry.Builder<T>> pickaxeOnly(){
        return mineableRequiriesTool(BlockTags.PICKAXE_MINEABLE);
    }
    static  <T extends Block> Consumer<BlockEntry.Builder<T>> axeOnly(){
        return mineableRequiriesTool(BlockTags.PICKAXE_MINEABLE);
    }
    static  <T extends Block> Consumer<BlockEntry.Builder<T>> shovelOnly(){
        return mineableRequiriesTool(BlockTags.PICKAXE_MINEABLE);
    }
    @SafeVarargs
    static <T extends Block> Consumer<BlockEntry.Builder<T>> mineableOptionalTool(TagKey<Block>... blockTag){
        return tBuilder -> tBuilder.tag(blockTag);
    }
    static  <T extends Block> Consumer<BlockEntry.Builder<T>> pickaxeHelps(){
        return mineableOptionalTool(BlockTags.PICKAXE_MINEABLE);
    }
    static  <T extends Block> Consumer<BlockEntry.Builder<T>> axeHelps(){
        return mineableOptionalTool(BlockTags.PICKAXE_MINEABLE);
    }
    static  <T extends Block> Consumer<BlockEntry.Builder<T>> shovelHelps(){
        return mineableOptionalTool(BlockTags.PICKAXE_MINEABLE);
    }
}
