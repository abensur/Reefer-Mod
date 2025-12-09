package com.abensur.badhabits;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = BadHabits.MODID, value = Dist.CLIENT)
public class ClientKeyInputHandler {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (ModKeyBindings.ACTIVATE_TEAR_LOCKER.get().consumeClick()) {
            // Send packet to server to activate Tear Locker
            PacketDistributor.sendToServer(new ActivateTearLockerPacket());
        }
    }
}
