package me.glicz.inventoryapi;

import lombok.Getter;
import lombok.experimental.Accessors;
import me.glicz.inventoryapi.event.paginated.GlitchInventoryPageChangeEvent;
import me.glicz.inventoryapi.item.gui.GuiItem;
import me.glicz.inventoryapi.item.gui.InventoryGuiItem;
import me.glicz.inventoryapi.util.margin.Margin;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Getter
@Accessors(fluent = true)
public class PaginatedGlitchInventoryImpl extends GlitchInventoryImpl<PaginatedGlitchInventory> implements PaginatedGlitchInventory {
    private List<GuiItem> paginationItems = new ArrayList<>();
    private List<GuiItem> currentPageItems = new ArrayList<>();
    private Margin margin = Margin.zero();
    private int pageCount;
    private int page;
    private Consumer<GlitchInventoryPageChangeEvent> pageChangeAction;

    public PaginatedGlitchInventoryImpl(int rows) {
        super(rows);
    }

    public PaginatedGlitchInventoryImpl(InventoryType inventoryType) {
        super(inventoryType);
    }

    @Override
    public PaginatedGlitchInventory open(@NotNull Player player, boolean reopen) {
        super.open(player, reopen);

        if (pageChangeAction != null) {
            pageChangeAction.accept(new GlitchInventoryPageChangeEvent(player, this, 0));
        }
        this.page = 0;
        updateCurrentPageItems();

        return this;
    }

    @Override
    public @NotNull List<@NotNull GuiItem> paginationItems() {
        return List.copyOf(paginationItems);
    }

    @Override
    public PaginatedGlitchInventory paginationItems(@Nullable List<@NotNull GuiItem> paginationItems) {
        if (paginationItems == null) {
            this.paginationItems = new ArrayList<>();
        } else {
            this.paginationItems = paginationItems.stream()
                    .map(guiItem -> (GuiItem) new InventoryGuiItem<>(this, -1, guiItem))
                    .toList();
        }

        updatePageCount();
        updateCurrentPageItems();

        return this;
    }

    @Override
    public @NotNull List<@NotNull GuiItem> currentPageItems() {
        return List.copyOf(currentPageItems);
    }

    private void updateCurrentPageItems() {
        int rows = size() / 9;

        int slots = size() - margin.sumSlots(rows);
        List<GuiItem> subList = paginationItems.subList(page * slots, Math.min((page + 1) * slots + 1, paginationItems.size()));

        List<GuiItem> currentPageItems = new ArrayList<>(items());

        int i = 0;
        for (int y = margin.top(); y < rows - margin.bottom(); y++) {
            for (int x = 0; x < 9; x++) {
                if (x < margin.left() || x > 9 - margin.right() - 1) continue;
                if (subList.size() <= i) break;

                currentPageItems.set(y * 9 + x, subList.get(i));
                i++;
            }
        }

        this.currentPageItems = currentPageItems;
        updateItems();
    }

    @Override
    public PaginatedGlitchInventory margin(@Nullable Margin margin) {
        this.margin = Objects.requireNonNullElse(margin, Margin.zero());

        updatePageCount();
        updateCurrentPageItems();

        return this;
    }

    @SuppressWarnings("ConstantValue")
    @Override
    public PaginatedGlitchInventory page(@Range(from = 0, to = Integer.MAX_VALUE) int page) {
        if (page > pageCount - 1 || page < 0 || page == this.page) return this;

        if (pageChangeAction != null) {
            pageChangeAction.accept(new GlitchInventoryPageChangeEvent(viewer(), this, page));
        }

        this.page = page;
        updateCurrentPageItems();

        return null;
    }

    @Override
    public boolean hasNextPage() {
        return page < pageCount - 1;
    }

    @Override
    public PaginatedGlitchInventory nextPage() {
        page(page + 1);
        return this;
    }

    @Override
    public boolean hasPreviousPage() {
        return page > 0;
    }

    @Override
    public PaginatedGlitchInventory previousPage() {
        page(page - 1);
        return this;
    }

    private void updatePageCount() {
        this.pageCount = (int) Math.ceil((double) paginationItems.size() / (size() - margin.sumSlots(size() / 9)));
    }

    @Override
    public PaginatedGlitchInventory pageChangeAction(@Nullable Consumer<GlitchInventoryPageChangeEvent> pageChangeAction) {
        this.pageChangeAction = pageChangeAction;
        return this;
    }
}
