package me.glicz.inventoryapi.item.builder;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ItemBuilder extends AbstractItemBuilder<ItemBuilder, ItemMeta> {
    ItemBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ItemBuilder itemBuilder(@NotNull Material material) {
        return itemBuilder(new ItemStack(material));
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ItemBuilder itemBuilder(@NotNull ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull LeatherArmorBuilder leatherArmorBuilder(@NotNull Material material) {
        return leatherArmorBuilder(new ItemStack(material));
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull LeatherArmorBuilder leatherArmorBuilder(@NotNull ItemStack itemStack) {
        return new LeatherArmorBuilder(itemStack);
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull SkullBuilder skullBuilder() {
        return new SkullBuilder();
    }
}
