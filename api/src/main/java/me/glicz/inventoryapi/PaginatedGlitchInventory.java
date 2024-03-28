package me.glicz.inventoryapi;

import me.glicz.inventoryapi.event.paginated.GlitchInventoryPageChangeEvent;
import me.glicz.inventoryapi.item.gui.GuiItem;
import me.glicz.inventoryapi.util.margin.Margin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public interface PaginatedGlitchInventory extends GlitchInventory<PaginatedGlitchInventory> {
    @NotNull List<@NotNull GuiItem> paginationItems();

    PaginatedGlitchInventory paginationItems(@Nullable List<@NotNull GuiItem> paginationItems);

    @NotNull List<@NotNull GuiItem> currentPageItems();

    Margin margin();

    PaginatedGlitchInventory margin(@Nullable Margin margin);

    @Override
    default @NotNull List<@NotNull GuiItem> allItems() {
        List<GuiItem> items = new ArrayList<>(currentPageItems());
        items.addAll(extraItems());
        return List.copyOf(items);
    }

    int pageCount();

    int page();

    PaginatedGlitchInventory page(int page);

    boolean hasNextPage();

    PaginatedGlitchInventory nextPage();

    boolean hasPreviousPage();

    PaginatedGlitchInventory previousPage();

    Consumer<GlitchInventoryPageChangeEvent> pageChangeAction();

    PaginatedGlitchInventory pageChangeAction(@Nullable Consumer<GlitchInventoryPageChangeEvent> pageChangeAction);
}
