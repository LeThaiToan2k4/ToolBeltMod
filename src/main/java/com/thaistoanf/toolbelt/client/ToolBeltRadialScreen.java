package com.thaistoanf.toolbelt.client;

import com.thaistoanf.toolbelt.component.ModDataComponentTypes;
import com.thaistoanf.toolbelt.network.SwapItemPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class ToolBeltRadialScreen extends Screen {
    private static final Identifier TEXTURE = Identifier.of("toolbeltmod", "textures/gui/belt_gui.png");

    private final int slotCount;
    private final int beltSlot;
    private int hoveredSlot = -1;

    public ToolBeltRadialScreen(int slots, int beltSlot) {
        super(Text.literal("Tool Belt Radial Menu"));
        this.slotCount = slots;
        this.beltSlot = beltSlot;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        if (this.client == null || this.client.player == null)
            return;

        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int imageSize = 256;
        context.drawTexture(RenderLayer::getGuiTextured, TEXTURE, centerX - imageSize / 2, centerY - imageSize / 2, 0f,
                0f, imageSize, imageSize, imageSize, imageSize);

        ItemStack beltStack = this.client.player.getInventory().getStack(this.beltSlot);
        DefaultedList<ItemStack> contents = DefaultedList.ofSize(8, ItemStack.EMPTY);
        ContainerComponent component = beltStack.get(ModDataComponentTypes.BELT_CONTENTS);
        if (component != null) {
            component.copyTo(contents);
        }

        this.hoveredSlot = -1;
        int slotRadius = 55;
        int boxHalfSize = 13;

        for (int i = 0; i < slotCount; i++) {
            double sliceAngle = (i * (360.0 / slotCount)) - 90;
            double rad = Math.toRadians(sliceAngle);
            int boxCenterX = centerX + (int) (Math.cos(rad) * slotRadius);
            int boxCenterY = centerY + (int) (Math.sin(rad) * slotRadius);

            if (mouseX >= boxCenterX - boxHalfSize && mouseX <= boxCenterX + boxHalfSize &&
                    mouseY >= boxCenterY - boxHalfSize && mouseY <= boxCenterY + boxHalfSize) {
                this.hoveredSlot = i;
            }

            ItemStack itemInSlot = contents.get(i);
            if (!itemInSlot.isEmpty()) {
                context.drawItem(itemInSlot, boxCenterX - 8, boxCenterY - 8);
            } else {
                // Nếu trống thì hiện chữ EMPTY
                String emptyText = "EMPTY";
                context.drawText(this.textRenderer, emptyText, boxCenterX - this.textRenderer.getWidth(emptyText) / 2,
                        boxCenterY - 4, 0xAAAAAA, true);
            }

            if (i == this.hoveredSlot) {
                context.fill(boxCenterX - boxHalfSize, boxCenterY - boxHalfSize, boxCenterX + boxHalfSize,
                        boxCenterY + boxHalfSize, 0x88DDDDDD);

                // Hiện tên Item đang chọn hoặc chữ INSERT
                String topText = itemInSlot.isEmpty() ? "INSERT" : itemInSlot.getName().getString();
                context.drawText(this.textRenderer, topText, boxCenterX - this.textRenderer.getWidth(topText) / 2,
                        boxCenterY - (boxHalfSize + 10), 0xFFFFFF, true);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.client != null && this.client.player != null) {
            int keyCode = ToolBeltClient.openToolBeltKey.getDefaultKey().getCode();
            if (!InputUtil.isKeyPressed(this.client.getWindow().getHandle(), keyCode)) {
                if (this.hoveredSlot != -1) {
                    ClientPlayNetworking.send(new SwapItemPayload(this.beltSlot, this.hoveredSlot));
                }
                this.close();
            }
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}