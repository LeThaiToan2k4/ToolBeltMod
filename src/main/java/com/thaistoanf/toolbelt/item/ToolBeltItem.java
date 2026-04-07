package com.thaistoanf.toolbelt.item;

import com.thaistoanf.toolbelt.component.ModDataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import java.util.List; // QUAN TRỌNG: Phải có dòng này

public class ToolBeltItem extends Item {
    public ToolBeltItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        // Đọc số slot từ thắt lưng
        int slots = stack.getOrDefault(ModDataComponentTypes.MAX_SLOTS, 2);
        
        // Hiện Slots: X / 9
        tooltip.add(Text.literal("Slots: ")
                .append(Text.literal(slots + " / 9").formatted(Formatting.GREEN))
                .formatted(Formatting.GRAY));
        
        super.appendTooltip(stack, context, tooltip, type);
    }
}