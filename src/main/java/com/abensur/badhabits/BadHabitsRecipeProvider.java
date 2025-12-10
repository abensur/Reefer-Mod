package com.abensur.badhabits;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.crafting.Ingredient;
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

        // Artificial Seasoning recipe: Dyes + Clay + Paper -> Artificial Seasoning
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BadHabits.ARTIFICIAL_SEASONING.get())
                .pattern("WYR")
                .pattern(" C ")
                .pattern(" P ")
                .define('W', Items.WHITE_DYE)
                .define('Y', Items.YELLOW_DYE)
                .define('R', Items.RED_DYE)
                .define('C', Items.CLAY_BALL)
                .define('P', Items.PAPER)
                .unlockedBy("has_white_dye", has(Items.WHITE_DYE))
                .unlockedBy("has_yellow_dye", has(Items.YELLOW_DYE))
                .unlockedBy("has_red_dye", has(Items.RED_DYE))
                .unlockedBy("has_clay_ball", has(Items.CLAY_BALL))
                .unlockedBy("has_paper", has(Items.PAPER))
                .save(output);

        // Beast Template recipe
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BadHabits.BEAST_TEMPLATE.get())
                .pattern("SPS")
                .pattern("SCS")
                .pattern("SSS")
                .define('S', Items.ARMADILLO_SCUTE)
                .define('P', Items.BAKED_POTATO)
                .define('C', Items.COOKED_CHICKEN)
                .unlockedBy("has_armadillo_scute", has(Items.ARMADILLO_SCUTE))
                .unlockedBy("has_baked_potato", has(Items.BAKED_POTATO))
                .unlockedBy("has_cooked_chicken", has(Items.COOKED_CHICKEN))
                .save(output);

        // Empty Tear Locker recipe: Heavy Core + 4 Rolling Papers (shaped)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BadHabits.EMPTY_TEAR_LOCKER_BLOCK_ITEM.get())
                .pattern(" R ")
                .pattern("RHR")
                .pattern(" R ")
                .define('R', BadHabits.ROLLING_PAPER.get())
                .define('H', Items.HEAVY_CORE)
                .unlockedBy("has_heavy_core", has(Items.HEAVY_CORE))
                .unlockedBy("has_rolling_paper", has(BadHabits.ROLLING_PAPER.get()))
                .save(output);

        // Dragon Builder Amulet smithing recipe
        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(BadHabits.BEAST_TEMPLATE.get()),
                Ingredient.of(Items.CHAIN),
                Ingredient.of(Items.DRAGON_EGG),
                RecipeCategory.MISC,
                BadHabits.DRAGON_BUILDER_AMULET.get()
        )
                .unlocks("has_beast_template", has(BadHabits.BEAST_TEMPLATE.get()))
                .unlocks("has_chain", has(Items.CHAIN))
                .unlocks("has_dragon_egg", has(Items.DRAGON_EGG))
                .save(output, BadHabits.MODID + ":dragon_builder_amulet");

        // Endless Curry Pouch recipe: Enchanted Golden Apple + Golden ingredients + Bundle + Dragon's Breath (shaped)
        // Pattern:
        //   [Sugar] [Glowstone Dust] [Redstone Dust]
        //   [Golden Carrot] [Enchanted Golden Apple] [Glistering Melon]
        //   [Dragon's Breath] [Bundle] [Dragon's Breath]
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BadHabits.ENDLESS_CURRY_POUCH.get())
                .pattern("SGR")
                .pattern("CAM")
                .pattern("OBO")
                .define('S', Items.SUGAR)
                .define('G', Items.GLOWSTONE_DUST)
                .define('R', Items.REDSTONE)
                .define('C', Items.GOLDEN_CARROT)
                .define('A', Items.ENCHANTED_GOLDEN_APPLE)
                .define('M', Items.GLISTERING_MELON_SLICE)
                .define('B', Items.BUNDLE)
                .define('O', Items.DRAGON_BREATH)
                .unlockedBy("has_enchanted_golden_apple", has(Items.ENCHANTED_GOLDEN_APPLE))
                .unlockedBy("has_golden_carrot", has(Items.GOLDEN_CARROT))
                .unlockedBy("has_bundle", has(Items.BUNDLE))
                .save(output);

        // Icarus's Wing recipe:
        //   [ ] [Elytra] [ ]
        //   [Slime] [String] [Slime]
        //   [Feather] [ ] [Feather]
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BadHabits.ICARUS_WING.get())
                .pattern(" E ")
                .pattern("SPS")
                .pattern("F F")
                .define('E', Items.ELYTRA)
                .define('S', Items.SLIME_BALL)
                .define('P', Items.STRING)
                .define('F', Items.FEATHER)
                .unlockedBy("has_elytra", has(Items.ELYTRA))
                .unlockedBy("has_slime_ball", has(Items.SLIME_BALL))
                .unlockedBy("has_string", has(Items.STRING))
                .unlockedBy("has_feather", has(Items.FEATHER))
                .save(output);
    }
}
