package com.abensur.badhabits;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ActivateTearLockerPacket() implements CustomPacketPayload {
    @SuppressWarnings("null")
    public static final CustomPacketPayload.Type<ActivateTearLockerPacket> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(BadHabits.MODID, "activate_tear_locker"));

    public static final StreamCodec<FriendlyByteBuf, ActivateTearLockerPacket> STREAM_CODEC =
        StreamCodec.unit(new ActivateTearLockerPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ActivateTearLockerPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                TearLockerHandler.activate(serverPlayer);
            }
        });
    }
}
