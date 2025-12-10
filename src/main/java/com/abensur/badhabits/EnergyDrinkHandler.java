package com.abensur.badhabits;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public class EnergyDrinkHandler {
    public static void register(IEventBus modBus) {
        NeoForge.EVENT_BUS.addListener(EnergyDrinkHandler::onServerTick);
    }

    @SuppressWarnings("deprecation")
    private static void deactivateFlight(ServerPlayer player) {
        player.getAbilities().setFlyingSpeed(0.05F);
        player.getAbilities().mayfly = false;
        player.getAbilities().flying = false;
        player.onUpdateAbilities();
    }

    @SuppressWarnings("null")
    private static void onServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            // Handle Red Energy Drink (Flight)
            if (player.hasData(BadHabits.FLIGHT_END_TIME)) {
                long endTime = player.getData(BadHabits.FLIGHT_END_TIME);

                if (endTime > 0 && server.overworld().getGameTime() >= endTime) {
                    // Deactivate flight (only if not creative)
                    if (!player.isCreative() && !player.isSpectator()) {
                        deactivateFlight(player);

                        // Apply energy crash - FINAL EFFECTS (no particle effects)
                        player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600, 1, false, false, true)); // Weakness II for 30s, no particles

                        // Crash particles
                        ServerLevel level = (ServerLevel) player.level();
                        level.sendParticles(ParticleTypes.SMOKE,
                            player.getX(), player.getY() + 1, player.getZ(),
                            25, 0.3, 0.5, 0.3, 0.03);

                        // Deactivation/crash sound
                        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 0.5F, 0.8F);
                    }

                    // Reset timestamp
                    player.setData(BadHabits.FLIGHT_END_TIME, 0L);
                }
            }

            // Handle Green Energy Drink (Beast Mode)
            if (player.hasData(BadHabits.BEAST_MODE_END_TIME)) {
                long endTime = player.getData(BadHabits.BEAST_MODE_END_TIME);

                if (endTime > 0 && server.overworld().getGameTime() >= endTime) {
                    // Apply crash effects after beast mode ends
                    if (!player.isCreative() && !player.isSpectator()) {
                        // "Beast Tamed" - crash effects for 30 seconds (600 ticks)
                        player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 600, 1, false, false, true)); // Mining Fatigue II, no particles

                        // Crash particles
                        ServerLevel level = (ServerLevel) player.level();
                        level.sendParticles(ParticleTypes.SMOKE,
                            player.getX(), player.getY() + 1, player.getZ(),
                            25, 0.3, 0.5, 0.3, 0.03);

                        // Crash sound
                        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 0.5F, 0.6F);
                    }

                    // Reset timestamp
                    player.setData(BadHabits.BEAST_MODE_END_TIME, 0L);
                }
            }
        }
    }
}
