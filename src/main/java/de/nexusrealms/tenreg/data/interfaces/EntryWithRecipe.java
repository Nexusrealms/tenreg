package de.nexusrealms.tenreg.data.interfaces;

import net.minecraft.data.recipe.RecipeGenerator;

public interface EntryWithRecipe {
    void accept(RecipeGenerator generator);
}
