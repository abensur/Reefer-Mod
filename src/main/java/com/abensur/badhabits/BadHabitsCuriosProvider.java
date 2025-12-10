package com.abensur.badhabits;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;

public class BadHabitsCuriosProvider extends CuriosDataProvider {

    public BadHabitsCuriosProvider(String modId, PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
        super(modId, output, fileHelper, registries);
    }

    @Override
    public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {
        // Create the charm slot with size 1
        this.createSlot("charm")
            .size(1)
            .order(10);

        // Assign the charm slot to players
        this.createEntities("player")
            .addPlayer()
            .addSlots("charm");

        // Create an amulet slot for neck curios
        this.createSlot("amulet")
            .size(1)
            .order(12);

        this.createEntities("player")
            .addPlayer()
            .addSlots("amulet");

        // Create the belt slot with size 1
        this.createSlot("belt")
            .size(1)
            .order(20);

        // Assign the belt slot to players
        this.createEntities("player")
            .addPlayer()
            .addSlots("belt");

        // Create a back slot with size 1 for wing-type curios
        this.createSlot("back")
            .size(1)
            .order(15);

        // Assign the back slot to players
        this.createEntities("player")
            .addPlayer()
            .addSlots("back");
    }
}
