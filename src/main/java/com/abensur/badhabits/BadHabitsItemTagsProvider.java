package com.abensur.badhabits;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class BadHabitsItemTagsProvider extends ItemTagsProvider {
    // Curios charm slot tag
    @SuppressWarnings("null")
    private static final TagKey<Item> CHARM_TAG = ItemTags.create(ResourceLocation.parse("curios:charm"));

    public BadHabitsItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                     CompletableFuture<TagLookup<Block>> blockTagsProvider,
                                     @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagsProvider, BadHabits.MODID, existingFileHelper);
    }

    @SuppressWarnings("null")
    @Override
    protected void addTags(@Nonnull HolderLookup.Provider provider) {
        // Tag the tear locker as compatible with the charm slot
        tag(CHARM_TAG).add(BadHabits.TEAR_LOCKER.get());
    }
}
