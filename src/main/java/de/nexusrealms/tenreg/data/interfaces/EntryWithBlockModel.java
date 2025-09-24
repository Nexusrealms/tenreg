package de.nexusrealms.tenreg.data.interfaces;

import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;

public interface EntryWithBlockModel {
    void accept(BlockStateModelGenerator generator);
}
