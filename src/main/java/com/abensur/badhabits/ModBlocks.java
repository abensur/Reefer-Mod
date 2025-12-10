package com.abensur.badhabits;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    @SuppressWarnings("null")
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, BadHabits.MODID);

    public static final DeferredHolder<Block, EmptyTearLockerBlock> EMPTY_TEAR_LOCKER = BLOCKS.register("empty_tear_locker",
        EmptyTearLockerBlock::new);
}
