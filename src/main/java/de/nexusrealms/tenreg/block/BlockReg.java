package de.nexusrealms.tenreg.block;

import de.nexusrealms.tenreg.Reg;
import de.nexusrealms.tenreg.SubReg;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BlockReg extends SubReg<Block> {
    public BlockReg(Reg root) {
        super(root);
    }

    @Override
    public Registry<Block> getRegistry() {
        return Registries.BLOCK;
    }

    public <T extends Block> BlockEntry.Builder<T> builder(){
        return new BlockEntry.Builder<>(this);
    }
}
