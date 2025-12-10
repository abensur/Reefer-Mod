package com.abensur.badhabits;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class IcarusWingHandler {
    public static void register(IEventBus modBus) {
        NeoForge.EVENT_BUS.addListener(IcarusWingHandler::onPlayerTick);
    }

    @SuppressWarnings("null")
    public static void refreshFlightState(Player player) {
        if (player == null) {
            return;
        }

        var level = player.level();
        if (level == null || level.isClientSide()) {
            return;
        }

        boolean hasWing = hasWingEquipped(player);
        boolean flagActive = player.hasData(BadHabits.ICARUS_WING_FLIGHT) && player.getData(BadHabits.ICARUS_WING_FLIGHT);

        if (hasWing) {
            if (!flagActive) {
                grantFlight(player);
            }
        } else if (flagActive) {
            revokeFlight(player);
        }
    }

    private static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player == null) {
            return;
        }

        var level = player.level();
        if (level == null || level.isClientSide()) {
            return;
        }

        refreshFlightState(player);
    }

    @SuppressWarnings("null")
    private static boolean hasWingEquipped(Player player) {

        // Check if equipped in chest slot (like Elytra)
        if (player.getItemBySlot(EquipmentSlot.CHEST).is(BadHabits.ICARUS_WING.get())) {
            return true;
        }

        try {
            return CuriosApi.getCuriosInventory(player).map(handler ->
                handler.findFirstCurio(BadHabits.ICARUS_WING.get()).isPresent()
            ).orElse(false);
        } catch (Throwable ignored) {
            return false;
        }
    }

    @SuppressWarnings({ "null", "deprecation" })
    private static void grantFlight(Player player) {
        player.setData(BadHabits.ICARUS_WING_FLIGHT, true);

        player.getAbilities().mayfly = true;
        if (!player.isCreative() && !player.isSpectator()) {
            player.getAbilities().flying = true;
        }
        player.onUpdateAbilities();
    }

    @SuppressWarnings({ "null", "deprecation" })
    private static void revokeFlight(Player player) {
        player.setData(BadHabits.ICARUS_WING_FLIGHT, false);

        if (player.isCreative() || player.isSpectator()) {
            return;
        }

        var level = player.level();
        if (level == null) {
            return;
        }

        long currentTime = level.getGameTime();
        if (player.hasData(BadHabits.FLIGHT_END_TIME)) {
            long drinkEnd = player.getData(BadHabits.FLIGHT_END_TIME);
            if (drinkEnd > currentTime) {
                return;
            }
        }

        player.getAbilities().mayfly = false;
        player.getAbilities().flying = false;
        player.onUpdateAbilities();
    }
}
