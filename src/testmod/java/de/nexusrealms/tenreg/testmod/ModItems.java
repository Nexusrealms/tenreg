package de.nexusrealms.tenreg.testmod;

import de.nexusrealms.tenreg.item.ItemEntry;
import de.nexusrealms.tenreg.item.ItemReg;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.item.BrushItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;

public class ModItems {
    private static final ItemReg ITEMS = Testmod.REG.getItemReg();
    public static final ItemEntry<Item> TEST1 = ITEMS.builder()
            .constructor(Item::new)
            .simpleModel()
            .settings(new Item.Settings().maxCount(4))
            .settingsUpdater(Item.Settings::fireproof)
            .group(Items.CRAFTER, ItemGroups.REDSTONE)
            .name("Test 1")
            .tag(ItemTags.ANVIL)
            .build("test1");
    public static final ItemEntry<BrushItem> TEST2 = ITEMS.<BrushItem>builder()
            .constructor(BrushItem::new)
            .simpleModel()
            .settings(new Item.Settings().maxCount(15))
            .settingsUpdater(settings -> settings.fireproof().food(FoodComponents.BEEF))
            .group(Items.CRAFTER, ItemGroups.REDSTONE)
            .name("Test 1")
            .tag(ItemTags.ANVIL)
            .build("test2");
    public static void init(){}
}
