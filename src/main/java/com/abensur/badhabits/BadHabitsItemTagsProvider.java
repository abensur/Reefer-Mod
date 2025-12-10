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
    // Curios slot tags
    @SuppressWarnings("null")
    private static final TagKey<Item> CHARM_TAG = ItemTags.create(ResourceLocation.parse("curios:charm"));
    @SuppressWarnings("null")
    private static final TagKey<Item> AMULET_TAG = ItemTags.create(ResourceLocation.parse("curios:amulet"));
    @SuppressWarnings("null")
    private static final TagKey<Item> BELT_TAG = ItemTags.create(ResourceLocation.parse("curios:belt"));
    @SuppressWarnings("null")
    private static final TagKey<Item> BACK_TAG = ItemTags.create(ResourceLocation.parse("curios:back"));

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

        // Tag the Dragon Builder Amulet for the amulet slot
        tag(AMULET_TAG).add(BadHabits.DRAGON_BUILDER_AMULET.get());

        // Tag the Endless Curry Pouch as compatible with the belt slot
        tag(BELT_TAG).add(BadHabits.ENDLESS_CURRY_POUCH.get());

        // Tag Icarus's Wing as compatible with the back slot
        tag(BACK_TAG).add(BadHabits.ICARUS_WING.get());
    }
}
