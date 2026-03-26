package com.thaistoanf.toolbelt.mixin;

import com.thaistoanf.toolbelt.component.ModDataComponentTypes;
import com.thaistoanf.toolbelt.item.ModItems; 

import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// KHÔNG extends nữa để tránh hoàn toàn lỗi Constructor!
@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {

    @Shadow @Final private Property levelCost;

    @Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
    private void toolbelt$upgradeWithPouch(CallbackInfo ci) {
        // Ép kiểu this thành AnvilScreenHandler để lấy các ô đồ
        AnvilScreenHandler handler = (AnvilScreenHandler) (Object) this;
        
        ItemStack leftStack = handler.getSlot(0).getStack();  // Ô bên trái
        ItemStack rightStack = handler.getSlot(1).getStack(); // Ô bên phải

        // Kiểm tra đúng Item chưa
        if (leftStack.isOf(ModItems.TOOL_BELT) && rightStack.isOf(ModItems.UPGRADE_POUCH)) {
            
            // Chỉ nâng cấp khi bỏ 1 cái Pouch
            if (rightStack.getCount() == 1) {
                int currentSlots = leftStack.getOrDefault(ModDataComponentTypes.MAX_SLOTS, 2);
                
                // Max 7 ô (2 gốc + 5 Pouch)
                if (currentSlots < 7) {
                    ItemStack outputStack = leftStack.copy();
                    outputStack.set(ModDataComponentTypes.MAX_SLOTS, currentSlots + 1);

                    // Xuất đồ ra ô thành phẩm (Ô số 2)
                    handler.getSlot(2).inventory.setStack(0, outputStack);
                    
                    // Trừ 10 level
                    this.levelCost.set(10); 
                    ci.cancel(); 
                }
            }
        }
    }
}