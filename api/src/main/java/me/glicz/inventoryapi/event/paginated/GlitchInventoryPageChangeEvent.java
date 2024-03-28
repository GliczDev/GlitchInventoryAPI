package me.glicz.inventoryapi.event.paginated;

import me.glicz.inventoryapi.PaginatedGlitchInventory;
import me.glicz.inventoryapi.event.GlitchInventoryEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

public final class GlitchInventoryPageChangeEvent extends GlitchInventoryEvent<PaginatedGlitchInventory> {
    private final int newPage;
    private final boolean hasNext, hasPrevious;

    @ApiStatus.Internal
    public GlitchInventoryPageChangeEvent(Player player, PaginatedGlitchInventory glitchInventory, int newPage) {
        super(player, glitchInventory);
        this.newPage = newPage;
        this.hasNext = newPage < glitchInventory.pageCount() - 1;
        this.hasPrevious = newPage > 0;
    }

    public int newPage() {
        return newPage;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public boolean hasPrevious() {
        return hasPrevious;
    }
}
