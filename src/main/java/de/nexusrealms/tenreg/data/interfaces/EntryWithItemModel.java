package de.nexusrealms.tenreg.data.interfaces;

import net.minecraft.client.data.ItemModelGenerator;

public interface EntryWithItemModel {
    void accept(ItemModelGenerator generator);
}
