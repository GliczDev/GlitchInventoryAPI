package me.glicz.inventoryapi.item.gui;

import me.glicz.inventoryapi.GlitchInventory;
import me.glicz.inventoryapi.click.ClickAction;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface GuiItem {
    @Contract(value = "_ -> new", pure = true)
    static @NotNull GuiItem guiItem(@NotNull ItemStack itemStack) {
        return guiItem(itemStack, null);
    }

    @Contract(value = "_, _ -> new", pure = true)
    static <T extends GlitchInventory<T>> @NotNull GuiItem guiItem(@NotNull ItemStack itemStack, @Nullable ClickAction<T> clickAction) {
        return new SimpleGuiItem(itemStack, clickAction);
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull GuiItem guiItem(@NotNull GuiItem guiItem) {
        return new SimpleGuiItem(guiItem.itemStack(), guiItem.clickAction());
    }

    @NotNull ItemStack itemStack();

    void itemStack(@NotNull ItemStack itemStack);

    default void mapItemStack(@NotNull Consumer<ItemStack> mapper) {
        ItemStack itemStack = itemStack();
        mapper.accept(itemStack);
        itemStack(itemStack);
    }

    @Nullable <T extends GlitchInventory<T>> ClickAction<T> clickAction();

    <T extends GlitchInventory<T>> void clickAction(ClickAction<T> clickAction);
}
