package com.abensur.badhabits;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public class MSGHandler {
    public static void register(IEventBus modBus) {
        NeoForge.EVENT_BUS.addListener(MSGHandler::onServerTick);
        NeoForge.EVENT_BUS.addListener(MSGHandler::onFoodEaten);
    }

    @SuppressWarnings("null")
    private static void onServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.hasData(BadHabits.MSG_BUFF_END_TIME)) {
                long endTime = player.getData(BadHabits.MSG_BUFF_END_TIME);

                // Check if buff has expired
                if (endTime > 0 && server.overworld().getGameTime() >= endTime) {
                    // Reset timestamp (buff expired without being used)
                    player.setData(BadHabits.MSG_BUFF_END_TIME, 0L);
                }
            }
        }
    }

    @SuppressWarnings("null")
    private static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide()) {
            ItemStack itemStack = event.getItem();

            // Check if player has MSG buff active
            if (player.hasData(BadHabits.MSG_BUFF_END_TIME)) {
                long endTime = player.getData(BadHabits.MSG_BUFF_END_TIME);
                long currentTime = player.level().getGameTime();

                // Only apply bonus if buff is still active
                if (endTime > 0 && currentTime < endTime) {
                    // Check if the item is food
                    FoodProperties foodProperties = itemStack.getFoodProperties(player);

                    if (foodProperties != null) {
                        // Add the same nutrition again to double it
                        int nutrition = foodProperties.nutrition();
                        float saturation = foodProperties.saturation();

                        player.getFoodData().eat(nutrition, saturation);

                        // Clear the buff after use
                        player.setData(BadHabits.MSG_BUFF_END_TIME, 0L);

                        // Play success sound
                        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                            net.minecraft.sounds.SoundEvents.PLAYER_LEVELUP,
                            net.minecraft.sounds.SoundSource.PLAYERS, 0.3F, 2.0F);
                    }
                }
            }
        }
    }
}
