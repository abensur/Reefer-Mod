package com.abensur.badhabits;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class BadHabitsRecipeProvider extends RecipeProvider {

    public BadHabitsRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    @SuppressWarnings("null")
    protected void buildRecipes(@Nonnull RecipeOutput output) {
        // Rolling Paper recipe: Paper + Slime Ball -> 32 Rolling Papers (slime ball always on top)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BadHabits.ROLLING_PAPER.get(), 32)
                .pattern("S")
                .pattern("P")
                .define('P', (ItemLike) Items.PAPER)
                .define('S', (ItemLike) Items.SLIME_BALL)
                .unlockedBy("has_paper", has(Items.PAPER))
                .unlockedBy("has_slime_ball", has(Items.SLIME_BALL))
                .save(output);

        // Ground Grass recipe: Short Grass -> 2 Ground Grass
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BadHabits.GROUND_GRASS.get(), 2)
                .requires((ItemLike) Items.SHORT_GRASS)
                .unlockedBy("has_short_grass", has(Items.SHORT_GRASS))
                .save(output);

        // Reefer recipe: Ground Grass + Rolling Paper -> Reefer
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BadHabits.REEFER.get())
                .requires(BadHabits.GROUND_GRASS.get())
                .requires(BadHabits.ROLLING_PAPER.get())
                .unlockedBy("has_ground_grass", has(BadHabits.GROUND_GRASS.get()))
                .unlockedBy("has_rolling_paper", has(BadHabits.ROLLING_PAPER.get()))
                .save(output);

        // Energy Drink recipe: 6x Iron Nugget + 1x Redstone + 2x Sugar -> Energy Drink
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BadHabits.ENERGY_DRINK.get())
                .pattern("NRN")
                .pattern("NSN")
                .pattern("NSN")
                .define('N', (ItemLike) Items.IRON_NUGGET)
                .define('R', (ItemLike) Items.REDSTONE)
                .define('S', (ItemLike) Items.SUGAR)
                .unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET))
                .unlockedBy("has_redstone", has(Items.REDSTONE))
                .unlockedBy("has_sugar", has(Items.SUGAR))
                .save(output);

        // MSGG recipe: Any Mushroom + Seagrass + Ground Grass -> MSGG
        // Using Ingredient.of() to accept multiple mushroom types including modded ones
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BadHabits.MSGG.get())
                .requires(Ingredient.of(
                    Items.BROWN_MUSHROOM,
                    Items.RED_MUSHROOM,
                    Items.CRIMSON_FUNGUS,
                    Items.WARPED_FUNGUS
                ))
                .requires((ItemLike) Items.SEAGRASS)
                .requires(BadHabits.GROUND_GRASS.get())
                .unlockedBy("has_mushroom", has(Items.BROWN_MUSHROOM))
                .unlockedBy("has_red_mushroom", has(Items.RED_MUSHROOM))
                .unlockedBy("has_seagrass", has(Items.SEAGRASS))
                .unlockedBy("has_ground_grass", has(BadHabits.GROUND_GRASS.get()))
                .save(output);
    }
}
