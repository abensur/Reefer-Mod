package com.abensur.badhabits;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class CurryPouchHandler {
    public static void register(IEventBus modBus) {
        NeoForge.EVENT_BUS.addListener(CurryPouchHandler::onFoodEaten);
    }

    @SuppressWarnings("null")
    private static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide()) {
            ItemStack itemStack = event.getItem();

            // Check if the item is food
            FoodProperties foodProperties = itemStack.getFoodProperties(player);
            if (foodProperties == null) {
                return;
            }

            // Check if player has the Endless Curry Pouch equipped in belt slot or inventory
            boolean hasCurryPouch = false;

            // Try Curios API first (belt slot)
            try {
                hasCurryPouch = CuriosApi.getCuriosInventory(player).map(handler -> {
                    return handler.findFirstCurio(BadHabits.ENDLESS_CURRY_POUCH.get()).isPresent();
                }).orElse(false);
            } catch (Exception e) {
                // Curios not available, fall through to inventory check
            }

            // Fall back to inventory check
            if (!hasCurryPouch) {
                hasCurryPouch = player.getInventory().contains(new ItemStack(BadHabits.ENDLESS_CURRY_POUCH.get()));
            }

            // If player has the Endless Curry Pouch, double the food
            if (hasCurryPouch) {
                int nutrition = foodProperties.nutrition();
                float saturation = foodProperties.saturation();

                player.getFoodData().eat(nutrition, saturation);

                // Play subtle success sound
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    net.minecraft.sounds.SoundEvents.PLAYER_BURP,
                    net.minecraft.sounds.SoundSource.PLAYERS, 0.5F, 1.2F);
            }
        }
    }
}
