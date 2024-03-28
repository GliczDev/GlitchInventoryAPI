package me.glicz.inventoryapi.event;

import me.glicz.inventoryapi.GlitchInventory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public abstract class GlitchInventoryEvent<T extends GlitchInventory<T>> {
    private final Player player;
    private final T glitchInventory;

    @ApiStatus.Internal
    public GlitchInventoryEvent(Player player, T glitchInventory) {
        this.player = player;
        this.glitchInventory = glitchInventory;
    }

    public @NotNull T glitchInventory() {
        return glitchInventory;
    }

    public @NotNull Player player() {
        return player;
    }
}
