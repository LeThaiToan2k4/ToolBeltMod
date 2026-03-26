package com.thaistoanf.toolbelt.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;

import org.lwjgl.glfw.GLFW;

import com.thaistoanf.toolbelt.component.ModDataComponentTypes;
import com.thaistoanf.toolbelt.item.ModItems;

public class ToolBeltClient implements ClientModInitializer {
    public static KeyBinding openToolBeltKey;

    @Override
    public void onInitializeClient() {
        openToolBeltKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.toolbelt.open",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.toolbelt.keys"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openToolBeltKey.wasPressed()) {
                if (client.currentScreen == null && client.player != null) {
                    // Tìm thắt lưng trong toàn bộ Inventory
                    int beltSlot = -1;
                    ItemStack beltStack = ItemStack.EMPTY;

                    for (int i = 0; i < client.player.getInventory().size(); i++) {
                        ItemStack stack = client.player.getInventory().getStack(i);
                        if (stack.isOf(ModItems.TOOL_BELT)) {
                            beltSlot = i;
                            beltStack = stack;
                            break; // Lấy cái thắt lưng đầu tiên tìm thấy
                        }
                    }

                    if (!beltStack.isEmpty()) {
                        int currentSlots = beltStack.getOrDefault(ModDataComponentTypes.MAX_SLOTS, 2);
                        // Truyền cả beltSlot vào Screen để tí nữa biết đường mà lưu đồ vào đúng cái
                        // belt đó
                        client.setScreen(new ToolBeltRadialScreen(currentSlots, beltSlot));
                    }
                }
            }
        });
    }
}
