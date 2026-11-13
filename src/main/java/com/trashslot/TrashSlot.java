package com.trashslot;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;

public class TrashSlot implements ModInitializer {
    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(TrashPayload.ID, TrashPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(TrashPayload.ID, (payload, context) ->
            context.player().currentScreenHandler.setCursorStack(ItemStack.EMPTY));
    }
}
