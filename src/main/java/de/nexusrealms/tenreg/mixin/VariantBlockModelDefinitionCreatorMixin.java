package de.nexusrealms.tenreg.mixin;

import net.minecraft.block.Block;
import net.minecraft.client.data.PropertiesMap;
import net.minecraft.client.data.VariantsBlockModelDefinitionCreator;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.Set;

@Mixin(VariantsBlockModelDefinitionCreator.class)
public interface VariantBlockModelDefinitionCreatorMixin {
    @Invoker("<init>")
    static VariantsBlockModelDefinitionCreator invokeInit(Block block, List<VariantsBlockModelDefinitionCreator.Entry> variants, Set<Property<?>> definedProperties) {
        throw new AssertionError("Invoker");
    }
    @Mixin(VariantsBlockModelDefinitionCreator.Entry.class)
   interface EntryMixin {
        @Invoker("<init>")
        static VariantsBlockModelDefinitionCreator.Entry invokeInit(PropertiesMap propertiesMap, WeightedVariant weightedVariant) {
            throw new AssertionError("Invoker");
        }
    }
}
