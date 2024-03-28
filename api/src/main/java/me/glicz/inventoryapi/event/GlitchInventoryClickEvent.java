package me.glicz.inventoryapi.event;

import me.glicz.inventoryapi.GlitchInventory;
import me.glicz.inventoryapi.click.ClickType;
import me.glicz.inventoryapi.item.gui.GuiItem;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public final class GlitchInventoryClickEvent<T extends GlitchInventory<T>> extends GlitchInventoryEvent<T> {
    private final GuiItem guiItem;
    private final int slot;
    private final ClickType clickType;

    @ApiStatus.Internal
    public GlitchInventoryClickEvent(Player player, T glitchInventory, GuiItem guiItem, int slot, ClickType clickType) {
        super(player, glitchInventory);
        this.guiItem = guiItem;
        this.slot = slot;
        this.clickType = clickType;
    }

    public @NotNull GuiItem guiItem() {
        return guiItem;
    }

    public int slot() {
        return slot;
    }

    public @NotNull ClickType clickType() {
        return clickType;
    }
}
