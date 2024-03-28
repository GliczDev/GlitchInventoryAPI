package me.glicz.inventoryapi.item.gui;

import me.glicz.inventoryapi.GlitchInventory;
import me.glicz.inventoryapi.click.ClickAction;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class SimpleGuiItem implements GuiItem {
    protected ItemStack itemStack;
    protected ClickAction<? extends GlitchInventory<?>> clickAction;

    SimpleGuiItem(ItemStack itemStack, ClickAction<? extends GlitchInventory<?>> clickAction) {
        itemStack(itemStack);
        clickAction(clickAction);
    }

    @Override
    public @NotNull ItemStack itemStack() {
        return itemStack.clone();
    }

    @Override
    public void itemStack(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack.clone();
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable <T extends GlitchInventory<T>> ClickAction<T> clickAction() {
        try {
            return (ClickAction<T>) clickAction;
        } catch (ClassCastException ignored) {
            return null;
        }
    }

    @Override
    public <T extends GlitchInventory<T>> void clickAction(ClickAction<T> clickAction) {
        this.clickAction = clickAction;
    }
}
