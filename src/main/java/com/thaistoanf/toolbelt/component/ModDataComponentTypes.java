package com.thaistoanf.toolbelt.component;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModDataComponentTypes {

    // Thuộc tính lưu số lượng ô của thắt lưng (Kiểu Integer)
    public static final ComponentType<Integer> MAX_SLOTS = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        Identifier.of("toolbeltmod", "max_slots"),
        ComponentType.<Integer>builder().codec(Codec.INT).build());

    public static final ComponentType<ContainerComponent> BELT_CONTENTS = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        Identifier.of("toolbeltmod", "belt_contents"),
        ComponentType.<ContainerComponent>builder().codec(ContainerComponent.CODEC).build());

    public static void initialize() {
        // Hàm này để gọi lúc khởi động mod
    }
}