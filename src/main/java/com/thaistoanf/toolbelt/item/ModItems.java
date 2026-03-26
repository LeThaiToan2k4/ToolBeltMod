package com.thaistoanf.toolbelt.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import java.util.function.Function;

public class ModItems {
    
    // Đăng ký Tool Belt (Sử dụng class ToolBeltItem của bạn)
    public static final Item TOOL_BELT = register("tool_belt", settings -> new ToolBeltItem(settings.maxCount(1)));
    
    // Đăng ký Upgrade Pouch (Dùng Item mặc định, có thể stack 64)
    public static final Item UPGRADE_POUCH = register("upgrade_pouch", Item::new);

    // HÀM ĐĂNG KÝ CHUẨN 1.21.4 (Bơm ID vào trước khi tạo Item)
    private static Item register(String name, Function<Item.Settings, Item> factory) {
        // 1. Tạo ID (RegistryKey)
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of("toolbeltmod", name));
        
        // 2. Gắn ID vào Settings
        Item.Settings settings = new Item.Settings().registryKey(key);
        
        // 3. Đưa Settings đã có ID vào để tạo Item, sau đó đăng ký vào game
        return Registry.register(Registries.ITEM, key, factory.apply(settings));
    }

    public static void initialize() {
        // Hàm này gọi từ ToolBeltMain để nạp Item
    }
}