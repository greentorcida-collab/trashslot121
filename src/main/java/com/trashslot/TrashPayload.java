package com.trashslot;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record TrashPayload() implements CustomPayload {
    public static final CustomPayload.Id<TrashPayload> ID =
        new CustomPayload.Id<>(Identifier.of("trashslot", "trash"));

    public static final PacketCodec<RegistryByteBuf, TrashPayload> CODEC =
        PacketCodec.unit(new TrashPayload());

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
