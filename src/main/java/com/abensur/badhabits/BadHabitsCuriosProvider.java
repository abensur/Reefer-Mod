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
    }
}
