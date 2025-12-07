package com.abensur.reefer;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class ReeferRecipeProvider extends RecipeProvider {

    public ReeferRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    @SuppressWarnings("null")
    protected void buildRecipes(@Nonnull RecipeOutput output) {
        // Rolling Paper recipe: Paper + Slime Ball -> 32 Rolling Papers (slime ball always on top)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Reefer.ROLLING_PAPER.get(), 32)
                .pattern("S")
                .pattern("P")
                .define('P', (ItemLike) Items.PAPER)
                .define('S', (ItemLike) Items.SLIME_BALL)
                .unlockedBy("has_paper", has(Items.PAPER))
                .unlockedBy("has_slime_ball", has(Items.SLIME_BALL))
                .save(output);

        // Ground Grass recipe: Short Grass -> 2 Ground Grass
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Reefer.GROUND_GRASS.get(), 2)
                .requires((ItemLike) Items.SHORT_GRASS)
                .unlockedBy("has_short_grass", has(Items.SHORT_GRASS))
                .save(output);

        // Reefer recipe: Ground Grass + Rolling Paper -> Reefer
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Reefer.REEFER.get())
                .requires(Reefer.GROUND_GRASS.get())
                .requires(Reefer.ROLLING_PAPER.get())
                .unlockedBy("has_ground_grass", has(Reefer.GROUND_GRASS.get()))
                .unlockedBy("has_rolling_paper", has(Reefer.ROLLING_PAPER.get()))
                .save(output);
    }
}
