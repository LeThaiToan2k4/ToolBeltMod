package com.thaistoanf.toolbelt.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SwapItemPayload(int beltSlotInInventory, int beltSlotIndex) implements CustomPayload {
    public static final Id<SwapItemPayload> ID = new Id<>(Identifier.of("toolbeltmod", "swap_item"));
    
    public static final PacketCodec<RegistryByteBuf, SwapItemPayload> CODEC = PacketCodec.tuple(
        PacketCodecs.VAR_INT, SwapItemPayload::beltSlotInInventory,
        PacketCodecs.VAR_INT, SwapItemPayload::beltSlotIndex,
        SwapItemPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}