package de.nexusrealms.tenreg.testmod;

import de.nexusrealms.tenreg.block.BlockEntry;
import de.nexusrealms.tenreg.block.BlockReg;
import de.nexusrealms.tenreg.block.BlockToolTransformers;
import de.nexusrealms.tenreg.data.functions.BlockModeler;
import de.nexusrealms.tenreg.data.functions.ItemModeler;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroups;

public class ModBlocks {
    private static final BlockReg BLOCKS = Testmod.REG.getBlockReg();
    public static final BlockEntry.Builder<Block> TEST_BUILDER = BLOCKS.builder()
            .constructor(Block::new)
            .settingsUpdater(AbstractBlock.Settings::replaceable)
            .transform(BlockToolTransformers.pickaxeOnly())
            .dropsSelf()
            .model(BlockModeler.multiSimpleCubeAll("0", "1"))
            .simpleItem()
            .item((itemBuilder, blockBlockEntry) -> itemBuilder.model(ItemModeler.blockCubeAll("_0")))
            .group(ItemGroups.BUILDING_BLOCKS);
    public static final BlockEntry<Block> TEST_BLOCK_1 = TEST_BUILDER
            .name("Test block 1")
            .build("test_block_1");
    public static final BlockEntry<Block> TEST_BLOCK_2 = TEST_BUILDER
            .name("Test block 2")
            .build("test_block_2");
    public static final BlockEntry<Block> TEST_BLOCK_3 = TEST_BUILDER
            .simpleCubeAll()
            .name("Test block 3")
            .build("test_block_3");
    public static void init(){}

}
