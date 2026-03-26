package com.thaistoanf.toolbelt;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.text.Text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thaistoanf.toolbelt.component.ModDataComponentTypes;
import com.thaistoanf.toolbelt.item.ModItems;
import com.thaistoanf.toolbelt.network.SwapItemPayload;

public class ToolBeltMain implements ModInitializer {
    public static final String MOD_ID = "toolbeltmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // 1. Khởi tạo Data Component (Bộ nhớ số ô của thắt lưng) trước
        ModDataComponentTypes.initialize();

        // 2. Khởi tạo toàn bộ Items từ file ModItems
        ModItems.initialize();

        // 3. Thêm các Items vừa tạo vào Tab Tools trong chế độ Sáng tạo (Creative)
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
            content.add(ModItems.TOOL_BELT);
            content.add(ModItems.UPGRADE_POUCH);
        });

        // ĐĂNG KÝ NETWORKING
        PayloadTypeRegistry.playC2S().register(SwapItemPayload.ID, SwapItemPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SwapItemPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                ServerPlayerEntity player = context.player();
                ItemStack beltStack = player.getInventory().getStack(payload.beltSlotInInventory());

                if (beltStack.isOf(ModItems.TOOL_BELT)) {
                    DefaultedList<ItemStack> contents = DefaultedList.ofSize(8, ItemStack.EMPTY);
                    ContainerComponent currentComponent = beltStack.get(ModDataComponentTypes.BELT_CONTENTS);
                    if (currentComponent != null) {
                        currentComponent.copyTo(contents);
                    }

                    ItemStack itemInHand = player.getMainHandStack();
                    ItemStack itemInBelt = contents.get(payload.beltSlotIndex());

                    // Kiểm tra món đồ trên tay
                    if (!itemInHand.isEmpty() && !isAllowedTool(itemInHand)) {
                        player.sendMessage(Text.literal("Chỉ swap được Vũ khí hoặc Công cụ!").formatted(Formatting.RED), true);
                        return;
                    }

                    // Swap
                    player.setStackInHand(Hand.MAIN_HAND, itemInBelt);
                    contents.set(payload.beltSlotIndex(), itemInHand.copy());

                    beltStack.set(ModDataComponentTypes.BELT_CONTENTS, ContainerComponent.fromStacks(contents));
                }
            });
        });
    }

    private boolean isAllowedTool(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof SwordItem || item instanceof PickaxeItem || 
               item instanceof AxeItem || item instanceof ShovelItem || 
               item instanceof HoeItem || item instanceof TridentItem || 
               item instanceof BowItem || item instanceof CrossbowItem ||
               item instanceof MaceItem;
    }
}