package com.abensur.badhabits;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class DragonBuilderAmuletHandler {
    private static final int EFFECT_DURATION_TICKS = 60; // 3 seconds, re-applied each tick

    public static void register(IEventBus modBus) {
        NeoForge.EVENT_BUS.addListener(DragonBuilderAmuletHandler::onPlayerTick);
    }

    @SuppressWarnings("null")
    private static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player == null) {
            return;
        }

        var level = player.level();
        if (level == null || level.isClientSide()) {
            return;
        }

        if (!hasAmulet(player)) {
            return;
        }

        // Apply positive effects
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, EFFECT_DURATION_TICKS, 1, true, false, true));
        player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, EFFECT_DURATION_TICKS, 1, true, false, true));
    }

    @SuppressWarnings("null")
    private static boolean hasAmulet(Player player) {
        // Inventory check first
        if (player.getInventory().contains(new ItemStack(BadHabits.DRAGON_BUILDER_AMULET.get()))) {
            return true;
        }

        // Curios support
        try {
            return CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findFirstCurio(BadHabits.DRAGON_BUILDER_AMULET.get()).isPresent())
                .orElse(false);
        } catch (Throwable ignored) {
            return false;
        }
    }
}
