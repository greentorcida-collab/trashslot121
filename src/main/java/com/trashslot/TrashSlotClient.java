package com.trashslot;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class TrashSlotClient implements ClientModInitializer {
    private static final int SIZE = 18;

    private static int slotX(HandledScreen<?> screen) {
        int x = screen.x + screen.backgroundWidth + 4;
        if (x + SIZE > screen.width) {
            x = screen.x - SIZE - 4;
        }
        return x;
    }

    private static int slotY(HandledScreen<?> screen) {
        return screen.y + screen.backgroundHeight - 24;
    }

    private static boolean inside(HandledScreen<?> screen, double mx, double my) {
        int x = slotX(screen);
        int y = slotY(screen);
        return mx >= x && mx < x + SIZE && my >= y && my < y + SIZE;
    }

    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!(screen instanceof HandledScreen<?> handled) || screen instanceof CreativeInventoryScreen) {
                return;
            }

            ScreenEvents.afterRender(screen).register((s, context, mouseX, mouseY, tickDelta) -> {
                int x = slotX(handled);
                int y = slotY(handled);
                context.fill(x, y, x + SIZE, y + SIZE, 0xFF373737);
                context.fill(x + 1, y + 1, x + SIZE - 1, y + SIZE - 1, 0xFF8B8B8B);
                context.drawTextWithShadow(client.textRenderer, "X", x + 6, y + 5, 0xFFFF5555);
                if (inside(handled, mouseX, mouseY)) {
                    context.drawTooltip(client.textRenderer, Text.literal("Sampah"), mouseX, mouseY);
                }
            });

            ScreenMouseEvents.allowMouseClick(screen).register((s, mouseX, mouseY, button) -> {
                if (inside(handled, mouseX, mouseY)) {
                    MinecraftClient mc = MinecraftClient.getInstance();
                    if (mc.player != null && !mc.player.currentScreenHandler.getCursorStack().isEmpty()) {
                        mc.player.currentScreenHandler.setCursorStack(ItemStack.EMPTY);
                        ClientPlayNetworking.send(new TrashPayload());
                    }
                    return false;
                }
                return true;
            });
        });
    }
}
