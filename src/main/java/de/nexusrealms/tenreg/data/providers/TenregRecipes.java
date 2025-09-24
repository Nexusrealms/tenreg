package de.nexusrealms.tenreg.data.providers;

import de.nexusrealms.tenreg.data.interfaces.EntryWithRecipe;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.registry.RegistryWrapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TenregRecipes extends FabricRecipeProvider {
    private final List<EntryWithRecipe> generators;
    public TenregRecipes(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, List<EntryWithRecipe> recipeGenerator) {
        super(output, registriesFuture);
        this.generators = recipeGenerator;
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter recipeExporter) {
        return new Generator(wrapperLookup, recipeExporter, generators);
    }

    @Override
    public String getName() {
        return "Recipes (Tenreg)";
    }
    public static class Generator extends RecipeGenerator {
        private final List<EntryWithRecipe> generators;
        public void addGenerator(EntryWithRecipe generator){
            generators.add(generator);
        }
        private Generator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter, List<EntryWithRecipe> generators) {
            super(registries, exporter);
            this.generators = generators;
        }

        @Override
        public void generate() {
            generators.forEach(generator1 -> generator1.accept(this));
        }
    }
}
