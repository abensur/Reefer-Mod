package com.abensur.badhabits;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class TearLockerHandler {
    private static final int COOLDOWN_TICKS = 200; // 10 seconds
    private static final int IMMUNITY_TICKS = 100; // 5 seconds

    public static void register(IEventBus modBus) {
        NeoForge.EVENT_BUS.addListener(TearLockerHandler::onPlayerTick);
        NeoForge.EVENT_BUS.addListener(TearLockerHandler::onMobEffectApplicable);
    }

    @SuppressWarnings("null")
    private static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        // Only process on server side
        if (player.level().isClientSide()) {
            return;
        }

        long currentTime = player.level().getGameTime();

        // Check if immunity period has ended
        if (player.hasData(BadHabits.TEAR_LOCKER_IMMUNITY_END)) {
            long immunityEndTime = player.getData(BadHabits.TEAR_LOCKER_IMMUNITY_END);
            if (immunityEndTime > 0 && currentTime >= immunityEndTime) {
                player.setData(BadHabits.TEAR_LOCKER_IMMUNITY_END, 0L);
            }
        }
    }

    @SuppressWarnings("null")
    private static void onMobEffectApplicable(MobEffectEvent.Applicable event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide()) {
            // Check if player has immunity active
            if (player.hasData(BadHabits.TEAR_LOCKER_IMMUNITY_END)) {
                long immunityEndTime = player.getData(BadHabits.TEAR_LOCKER_IMMUNITY_END);
                long currentTime = player.level().getGameTime();

                if (immunityEndTime > 0 && currentTime < immunityEndTime) {
                    MobEffect effect = event.getEffectInstance().getEffect().value();
                    // Only block harmful effects
                    if (!effect.isBeneficial()) {
                        event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
                    }
                }
            }
        }
    }

    @SuppressWarnings("null")
    public static void activate(ServerPlayer player) {
        long currentTime = player.level().getGameTime();

        // Check if player has the Tear Locker item (either equipped in Curios or in inventory)
        boolean hasTearLocker = false;

        // First check Curios if available
        try {
            var curiosResult = CuriosApi.getCuriosInventory(player);
            if (curiosResult.isPresent()) {
                hasTearLocker = curiosResult.get().findFirstCurio(BadHabits.TEAR_LOCKER.get()).isPresent();
            }
        } catch (NoClassDefFoundError e) {
            // Curios not installed, skip this check
        }

        // If not found in Curios, check player inventory
        if (!hasTearLocker) {
            hasTearLocker = player.getInventory().contains(BadHabits.TEAR_LOCKER.get().getDefaultInstance());
        }

        if (!hasTearLocker) {
            player.displayClientMessage(Component.translatable("message.badhabits.tear_locker_not_found"), true);

            // Play error sound
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.VILLAGER_NO, SoundSource.PLAYERS, 0.5F, 1.0F);

            return;
        }

        // Check cooldown
        if (player.hasData(BadHabits.TEAR_LOCKER_COOLDOWN)) {
            long cooldownEndTime = player.getData(BadHabits.TEAR_LOCKER_COOLDOWN);
            if (currentTime < cooldownEndTime) {
                long remainingTicks = cooldownEndTime - currentTime;
                int remainingSeconds = (int) (remainingTicks / 20);
                player.displayClientMessage(Component.translatable("message.badhabits.tear_locker_cooldown", remainingSeconds), true);

                // Play failure sound
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.5F, 1.8F);

                return;
            }
        }

        // Clear all negative effects
        player.getActiveEffects().stream()
            .filter(effectInstance -> !effectInstance.getEffect().value().isBeneficial())
            .map(effectInstance -> effectInstance.getEffect())
            .toList() // Create a copy to avoid ConcurrentModificationException
            .forEach(player::removeEffect);

        // Set cooldown
        player.setData(BadHabits.TEAR_LOCKER_COOLDOWN, currentTime + COOLDOWN_TICKS);

        // Set item cooldown visual (shows on the item in hotbar/inventory)
        player.getCooldowns().addCooldown(BadHabits.TEAR_LOCKER.get(), COOLDOWN_TICKS);

        // Set immunity period
        player.setData(BadHabits.TEAR_LOCKER_IMMUNITY_END, currentTime + IMMUNITY_TICKS);

        // Play layered success sounds for richer audio feedback
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
            SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.PLAYERS, 0.8F, 1.5F);

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
            SoundEvents.NOTE_BLOCK_BELL.value(), SoundSource.PLAYERS, 0.6F, 2.0F);

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
            SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.5F, 1.2F);

        // Spawn particles
        if (player.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                player.getX(), player.getY() + 1, player.getZ(),
                20, 0.5, 0.5, 0.5, 0.1);

            serverLevel.sendParticles(ParticleTypes.SOUL,
                player.getX(), player.getY() + 1, player.getZ(),
                10, 0.3, 0.5, 0.3, 0.05);
        }

        // Send feedback message
        player.displayClientMessage(Component.translatable("message.badhabits.tear_locker_activated"), true);
    }
}
