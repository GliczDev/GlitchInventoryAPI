package me.glicz.inventoryapi.event;

import me.glicz.inventoryapi.GlitchInventory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

public final class GlitchInventoryOpenEvent<T extends GlitchInventory<T>> extends GlitchInventoryEvent<T> {
    @ApiStatus.Internal
    public GlitchInventoryOpenEvent(Player player, T glitchInventory) {
        super(player, glitchInventory);
    }
}
