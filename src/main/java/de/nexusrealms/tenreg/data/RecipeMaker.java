package de.nexusrealms.tenreg.data;

import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryEntryLookup;

@FunctionalInterface
public interface RecipeMaker<T> {
    void generate(RecipeGenerator generator, T t);
}
