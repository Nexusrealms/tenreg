package de.nexusrealms.tenreg.data.functions;

import de.nexusrealms.tenreg.data.RecipeMaker;
import de.nexusrealms.tenreg.item.ItemEntry;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;

import static net.minecraft.data.recipe.RecipeGenerator.hasItem;

public interface Recipes {
    static <T extends Item> RecipeMaker<ItemEntry<T>> stonecutting(int count, RecipeCategory category, ItemConvertible... bases) {
        return (generator, tItemEntry) -> {
            for(ItemConvertible base : bases){
                generator.offerStonecuttingRecipe(category, tItemEntry, base, count);
            }
        };
    }
    static <T extends Item> RecipeMaker<ItemEntry<T>> stonecutting(RecipeCategory category, ItemConvertible... bases) {
        return stonecutting(1, category, bases);
    }
    static <T extends Item> RecipeMaker<ItemEntry<T>> stonePolishingWithStonecutting(ItemConvertible base, RecipeCategory category, ItemConvertible... altStonecuttingBases) {
        return (generator, tItemEntry) -> {
            ItemConvertible[] newArray = new ItemConvertible[altStonecuttingBases.length + 1];
            newArray[0] = base;
            System.arraycopy(altStonecuttingBases, 0, newArray, 1, altStonecuttingBases.length);
            stonecutting(category, newArray).generate(generator, (ItemEntry<Item>) tItemEntry);
            generator.offerPolishedStoneRecipe(category, tItemEntry, base);
        };
    }
    static <T extends Item> RecipeMaker<ItemEntry<T>> slabWithStonecutting(ItemConvertible base, RecipeCategory category, ItemConvertible... altStonecuttingBases) {
        return (generator, tItemEntry) -> {
            ItemConvertible[] newArray = new ItemConvertible[altStonecuttingBases.length + 1];
            newArray[0] = base;
            System.arraycopy(altStonecuttingBases, 0, newArray, 1, altStonecuttingBases.length);
            stonecutting(category, newArray).generate(generator, (ItemEntry<Item>) tItemEntry);
            generator.offerSlabRecipe(category, tItemEntry, base);
        };
    }
    static <T extends Item> RecipeMaker<ItemEntry<T>> stairsWithStonecutting(ItemConvertible base, RecipeCategory category, ItemConvertible... altStonecuttingBases) {
        return (generator, tItemEntry) -> {
            ItemConvertible[] newArray = new ItemConvertible[altStonecuttingBases.length + 1];
            newArray[0] = base;
            System.arraycopy(altStonecuttingBases, 0, newArray, 1, altStonecuttingBases.length);
            stonecutting(category, newArray).generate(generator, (ItemEntry<Item>) tItemEntry);
            generator.createStairsRecipe(tItemEntry, Ingredient.ofItem(base)).criterion(hasItem(base), generator.conditionsFromItem(base)).offerTo(generator.exporter);
        };
    }

    static <T extends Item> RecipeMaker<ItemEntry<T>> wallsWithStonecutting(ItemConvertible base, RecipeCategory category, ItemConvertible... altStonecuttingBases) {
        return (generator, tItemEntry) -> {
            ItemConvertible[] newArray = new ItemConvertible[altStonecuttingBases.length + 1];
            newArray[0] = base;
            System.arraycopy(altStonecuttingBases, 0, newArray, 1, altStonecuttingBases.length);
            stonecutting(category, newArray).generate(generator, (ItemEntry<Item>) tItemEntry);
            generator.getWallRecipe(category, tItemEntry, Ingredient.ofItem(base)).criterion(hasItem(base), generator.conditionsFromItem(base)).offerTo(generator.exporter);
        };
    }
    static <T extends Item> RecipeMaker<ItemEntry<T>> button(ItemConvertible base, RecipeCategory category) {
        return (generator, tItemEntry) -> {
            generator.createButtonRecipe(tItemEntry, Ingredient.ofItem(base)).criterion(hasItem(base), generator.conditionsFromItem(base)).offerTo(generator.exporter);
        };
    }
    static <T extends Item> RecipeMaker<ItemEntry<T>> pressurePlate(ItemConvertible base, RecipeCategory category) {
        return (generator, tItemEntry) -> {
            generator.createPressurePlateRecipe(category, tItemEntry, Ingredient.ofItem(base)).criterion(hasItem(base), generator.conditionsFromItem(base)).offerTo(generator.exporter);
        };
    }
    static <T extends Item> RecipeMaker<ItemEntry<T>> pillarWithStonecutting(ItemConvertible base, RecipeCategory category, ItemConvertible... altStonecuttingBases) {
        return (generator, tItemEntry) -> {
            ItemConvertible[] newArray = new ItemConvertible[altStonecuttingBases.length + 1];
            newArray[0] = base;
            System.arraycopy(altStonecuttingBases, 0, newArray, 1, altStonecuttingBases.length);
            stonecutting(category, newArray).generate(generator, (ItemEntry<Item>) tItemEntry);
            generator.createShaped(category, tItemEntry)
                    .input('#', base)
                    .pattern("#")
                    .pattern("#").criterion(hasItem(base), generator.conditionsFromItem(base)).offerTo(generator.exporter);
        };
    }
    static <T extends Item> RecipeMaker<ItemEntry<T>> pillarWithStonecuttingOnlyAlts(ItemConvertible base, RecipeCategory category, ItemConvertible... altStonecuttingBases) {
        return (generator, tItemEntry) -> {
            stonecutting(category, altStonecuttingBases).generate(generator, (ItemEntry<Item>) tItemEntry);
            generator.createShaped(category, tItemEntry)
                    .input('#', base)
                    .pattern("#")
                    .pattern("#").criterion(hasItem(base), generator.conditionsFromItem(base)).offerTo(generator.exporter);
        };
    }
}
