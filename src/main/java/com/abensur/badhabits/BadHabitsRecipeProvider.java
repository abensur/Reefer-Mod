package com.abensur.badhabits;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

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
        // Create a water bottle ItemStack with the proper data component
        ItemStack waterBottle = new ItemStack(Items.POTION);
        waterBottle.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BadHabits.ENERGY_DRINK_BASE.get())
                .pattern("NSN")
                .pattern("NWN")
                .pattern("NSN")
                .define('N', (ItemLike) Items.IRON_NUGGET)
                .define('S', (ItemLike) Items.SUGAR)
                .define('W', DataComponentIngredient.of(false, waterBottle)) // Water bottle - partial match
                .unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET))
                .unlockedBy("has_sugar", has(Items.SUGAR))
                .unlockedBy("has_water_bottle", has(Items.POTION))
                .save(output);

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

        // MSG recipe: Any Mushroom + Sugar + Gunpowder -> MSG
        // Using Ingredient.of() to accept multiple mushroom types including modded ones
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BadHabits.MSG.get())
                .requires(Ingredient.of(
                    Items.BROWN_MUSHROOM,
                    Items.RED_MUSHROOM,
                    Items.CRIMSON_FUNGUS,
                    Items.WARPED_FUNGUS
                ))
                .requires((ItemLike) Items.SUGAR)
                .requires((ItemLike) Items.GUNPOWDER)
                .unlockedBy("has_mushroom", has(Items.BROWN_MUSHROOM))
                .unlockedBy("has_red_mushroom", has(Items.RED_MUSHROOM))
                .unlockedBy("has_sugar", has(Items.SUGAR))
                .unlockedBy("has_gunpowder", has(Items.GUNPOWDER))
                .save(output);

        // Tear Locker recipe: Heavy Core + Ghast Tears + Rolling Papers (shaped)
        // Pattern:
        //   [ ] [Rolling Paper] [ ]
        //   [Ghast Tear] [Heavy Core] [Ghast Tear]
        //   [ ] [Rolling Paper] [ ]
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BadHabits.TEAR_LOCKER.get())
                .pattern(" R ")
                .pattern("THT")
                .pattern(" R ")
                .define('R', BadHabits.ROLLING_PAPER.get())
                .define('T', Items.GHAST_TEAR)
                .define('H', Items.HEAVY_CORE)
                .unlockedBy("has_heavy_core", has(Items.HEAVY_CORE))
                .unlockedBy("has_ghast_tear", has(Items.GHAST_TEAR))
                .unlockedBy("has_rolling_paper", has(BadHabits.ROLLING_PAPER.get()))
                .save(output);
    }
}
