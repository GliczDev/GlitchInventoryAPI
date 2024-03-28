package me.glicz.inventoryapi.item.builder;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;

public final class LeatherArmorBuilder extends AbstractItemBuilder<LeatherArmorBuilder, LeatherArmorMeta> {
    LeatherArmorBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
    }

    public Color color() {
        return itemMeta.getColor();
    }

    public LeatherArmorBuilder color(Color color) {
        itemMeta.setColor(color);
        return this;
    }
}
