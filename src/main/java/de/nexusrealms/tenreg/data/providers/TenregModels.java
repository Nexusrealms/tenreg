package de.nexusrealms.tenreg.data.providers;

import de.nexusrealms.tenreg.data.interfaces.EntryWithBlockModel;
import de.nexusrealms.tenreg.data.interfaces.EntryWithItemModel;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;

import java.util.ArrayList;
import java.util.List;

public class TenregModels extends FabricModelProvider {

    private final List<EntryWithBlockModel> blocks = new ArrayList<>();
    private final List<EntryWithItemModel> items = new ArrayList<>();

    public TenregModels(FabricDataOutput output) {
        super(output);
    }

    public void addBlock(EntryWithBlockModel generator){
        blocks.add(generator);
    }
    public void addItem(EntryWithItemModel generator){
        items.add(generator);
    }
    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blocks.forEach(generator -> generator.accept(blockStateModelGenerator));
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        items.forEach(entryWithItemModel -> entryWithItemModel.accept(itemModelGenerator));
    }
}
