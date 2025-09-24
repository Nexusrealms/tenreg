package de.nexusrealms.tenreg.item;

import de.nexusrealms.tenreg.Reg;
import de.nexusrealms.tenreg.RegEntry;
import de.nexusrealms.tenreg.SubReg;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ItemReg extends SubReg<Item> {
    public ItemReg(Reg root) {
        super(root);
    }

    @Override
    public Registry<Item> getRegistry() {
        return Registries.ITEM;
    }

    public <T extends Item> ItemEntry.Builder<T> builder(){
        return new ItemEntry.Builder<>(this);
    }
}
