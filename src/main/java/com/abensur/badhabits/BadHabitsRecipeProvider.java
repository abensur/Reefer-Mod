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

        // Energy Drink Base recipe: 6x Iron Nugget + 2x Sugar + 1x Water Bottle -> Energy Drink Base
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BadHabits.ENERGY_DRINK_BASE.get())
                .pattern("NSN")
                .pattern("NWN")
                .pattern("NSN")
                .define('N', (ItemLike) Items.IRON_NUGGET)
                .define('S', (ItemLike) Items.SUGAR)
                .define('W', (ItemLike) Items.POTION) // Water bottle
                .unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET))
                .unlockedBy("has_sugar", has(Items.SUGAR))
                .unlockedBy("has_water_bottle", has(Items.POTION))
                .save(output);

        // Red Energy Drink recipe: Energy Drink Base + Red Dye + Leather -> Red Energy Drink
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BadHabits.RED_ENERGY_DRINK.get())
                .requires(BadHabits.ENERGY_DRINK_BASE.get())
                .requires((ItemLike) Items.RED_DYE)
                .requires((ItemLike) Items.LEATHER)
                .unlockedBy("has_energy_drink_base", has(BadHabits.ENERGY_DRINK_BASE.get()))
                .unlockedBy("has_red_dye", has(Items.RED_DYE))
                .unlockedBy("has_leather", has(Items.LEATHER))
                .save(output);

        // Green Energy Drink recipe: Energy Drink Base + Green Dye + Rotten Flesh -> Green Energy Drink
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BadHabits.GREEN_ENERGY_DRINK.get())
                .requires(BadHabits.ENERGY_DRINK_BASE.get())
                .requires((ItemLike) Items.GREEN_DYE)
                .requires((ItemLike) Items.ROTTEN_FLESH)
                .unlockedBy("has_energy_drink_base", has(BadHabits.ENERGY_DRINK_BASE.get()))
                .unlockedBy("has_green_dye", has(Items.GREEN_DYE))
                .unlockedBy("has_rotten_flesh", has(Items.ROTTEN_FLESH))
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
