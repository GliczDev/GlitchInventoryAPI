package me.glicz.inventoryapi.click;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum ClickType {
    LEFT(0, 0, org.bukkit.event.inventory.ClickType.LEFT, Attribute.LEFT),
    RIGHT(0, 1, org.bukkit.event.inventory.ClickType.RIGHT, Attribute.RIGHT),
    SHIFT_LEFT(1, 0, org.bukkit.event.inventory.ClickType.SHIFT_LEFT, Attribute.LEFT, Attribute.SHIFT),
    SHIFT_RIGHT(1, 1, org.bukkit.event.inventory.ClickType.SHIFT_RIGHT, Attribute.RIGHT, Attribute.SHIFT),
    NUM_KEY_1(2, 0, org.bukkit.event.inventory.ClickType.NUMBER_KEY, Attribute.NUMBER_KEY),
    NUM_KEY_2(2, 1, org.bukkit.event.inventory.ClickType.NUMBER_KEY, Attribute.NUMBER_KEY),
    NUM_KEY_3(2, 2, org.bukkit.event.inventory.ClickType.NUMBER_KEY, Attribute.NUMBER_KEY),
    NUM_KEY_4(2, 3, org.bukkit.event.inventory.ClickType.NUMBER_KEY, Attribute.NUMBER_KEY),
    NUM_KEY_5(2, 4, org.bukkit.event.inventory.ClickType.NUMBER_KEY, Attribute.NUMBER_KEY),
    NUM_KEY_6(2, 5, org.bukkit.event.inventory.ClickType.NUMBER_KEY, Attribute.NUMBER_KEY),
    NUM_KEY_7(2, 6, org.bukkit.event.inventory.ClickType.NUMBER_KEY, Attribute.NUMBER_KEY),
    NUM_KEY_8(2, 7, org.bukkit.event.inventory.ClickType.NUMBER_KEY, Attribute.NUMBER_KEY),
    NUM_KEY_9(2, 8, org.bukkit.event.inventory.ClickType.NUMBER_KEY, Attribute.NUMBER_KEY),
    OFFHAND_SWAP(2, 40, org.bukkit.event.inventory.ClickType.SWAP_OFFHAND),
    MIDDLE(3, 2, org.bukkit.event.inventory.ClickType.MIDDLE),
    DROP(4, 0, org.bukkit.event.inventory.ClickType.DROP, Attribute.DROP),
    CTRL_DROP(4, 1, org.bukkit.event.inventory.ClickType.CONTROL_DROP, Attribute.DROP),
    DOUBLE(6, 0, org.bukkit.event.inventory.ClickType.DOUBLE_CLICK);

    private static final Map<Integer, Map<Integer, ClickType>> clickTypeMap = new HashMap<>();

    static {
        for (ClickType clickType : values()) {
            clickTypeMap.computeIfAbsent(clickType.mode, key -> new HashMap<>()).put(clickType.button, clickType);
        }
    }

    private final int mode;
    private final int button;
    private final org.bukkit.event.inventory.ClickType asBukkit;
    private final Set<Attribute> attributes;

    ClickType(int mode, int button, org.bukkit.event.inventory.ClickType asBukkit, Attribute... attributes) {
        this.mode = mode;
        this.button = button;
        this.asBukkit = asBukkit;
        this.attributes = Set.of(attributes);
    }

    public static ClickType get(int mode, int button) {
        if (mode == 5) {
            if (button <= 2) {
                return ClickType.LEFT;
            } else if (button <= 6) {
                return ClickType.RIGHT;
            } else if (button <= 10) {
                return ClickType.MIDDLE;
            }
        }

        return clickTypeMap.getOrDefault(mode, Map.of()).get(button);
    }

    public org.bukkit.event.inventory.ClickType asBukkit() {
        return asBukkit;
    }

    public boolean hasAttribute(Attribute attribute) {
        return attributes.contains(attribute);
    }

    public enum Attribute {
        LEFT,
        RIGHT,
        SHIFT,
        NUMBER_KEY,
        DROP
    }
}
