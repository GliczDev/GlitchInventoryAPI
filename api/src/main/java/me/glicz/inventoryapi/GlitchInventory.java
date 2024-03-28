package me.glicz.inventoryapi;

import me.glicz.inventoryapi.click.ClickAction;
import me.glicz.inventoryapi.click.ClickType;
import me.glicz.inventoryapi.event.GlitchInventoryCloseEvent;
import me.glicz.inventoryapi.event.GlitchInventoryOpenEvent;
import me.glicz.inventoryapi.factory.InvalidInventoryType;
import me.glicz.inventoryapi.item.builder.ItemBuilder;
import me.glicz.inventoryapi.item.gui.GuiItem;
import me.glicz.inventoryapi.title.Title;
import me.glicz.inventoryapi.util.NotNullConsumer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public interface GlitchInventory<T extends GlitchInventory<T>> {
    @Contract(value = "_ -> new", pure = true)
    static SimpleGlitchInventory simple(@Range(from = 1, to = 6) int rows) throws InvalidInventoryType {
        return GlitchInventoryAPI.get().factory().newInventory(SimpleGlitchInventory.class, rows);
    }

    @Contract(value = "_ -> new", pure = true)
    static SimpleGlitchInventory simple(InventoryType inventoryType) throws InvalidInventoryType {
        return GlitchInventoryAPI.get().factory().newInventory(SimpleGlitchInventory.class, inventoryType);
    }

    @Contract(value = "_ -> new", pure = true)
    static PaginatedGlitchInventory paginated(@Range(from = 1, to = 6) int rows) throws InvalidInventoryType {
        return GlitchInventoryAPI.get().factory().newInventory(PaginatedGlitchInventory.class, rows);
    }

    @Contract(value = "_ -> new", pure = true)
    static PaginatedGlitchInventory paginated(InventoryType inventoryType) throws InvalidInventoryType {
        return GlitchInventoryAPI.get().factory().newInventory(PaginatedGlitchInventory.class, inventoryType);
    }

    @Contract(value = "-> new", pure = true)
    static MerchantGlitchInventory merchant() throws InvalidInventoryType {
        return GlitchInventoryAPI.get().factory().newInventory(MerchantGlitchInventory.class);
    }

    static GlitchInventory<?> get(@NotNull Player player) {
        return GlitchInventoryAPI.get().getGlitchInventory(player);
    }

    int size();

    @NotNull InventoryType inventoryType();

    @NotNull List<@NotNull GuiItem> items();

    default GuiItem item(@Range(from = 0, to = Integer.MAX_VALUE) int slot) {
        try {
            return allItems().get(slot);
        } catch (IndexOutOfBoundsException ignored) {
            return null;
        }
    }

    default T item(@Range(from = 0, to = Integer.MAX_VALUE) int slot, @NotNull ItemStack itemStack) {
        return item(slot, GuiItem.guiItem(itemStack));
    }

    default T item(@Range(from = 0, to = Integer.MAX_VALUE) int slot, @NotNull ItemBuilder itemBuilder) {
        return item(slot, itemBuilder.asGuiItem());
    }

    T item(@Range(from = 0, to = Integer.MAX_VALUE) int slot, @Nullable GuiItem guiItem);

    @SuppressWarnings("unchecked")
    default T mapItem(@Range(from = 0, to = Integer.MAX_VALUE) int slot, @NotNull NotNullConsumer<GuiItem> mapper) {
        mapper.acceptIfNotNull(item(slot));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @ApiStatus.Experimental
    default T drawColumn(@Range(from = 0, to = Integer.MAX_VALUE) int column, GuiItem guiItem) {
        for (int i = 0; i < size() / 9; i++) {
            item(i * 9 + column, guiItem);
        }

        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @ApiStatus.Experimental
    default T drawRow(@Range(from = 0, to = Integer.MAX_VALUE) int row, @Nullable GuiItem guiItem) {
        for (int i = 0; i < 9; i++) {
            item(row * 9 + i, guiItem);
        }

        return (T) this;
    }

    boolean modifyPlayerInventory();

    @NotNull List<@NotNull GuiItem> extraItems();

    default @NotNull List<@NotNull GuiItem> allItems() {
        List<GuiItem> items = new ArrayList<>(items());
        items.addAll(extraItems());
        return List.copyOf(items);
    }

    T modifyPlayerInventory(boolean modifyPlayerInventory);

    T updateItems();

    T updateItem(int slot);

    Title<?> title();

    default T title(Component component) {
        return title(Title.simple(component));
    }

    T title(Title<?> title);

    Player viewer();

    @ApiStatus.Internal
    Integer containerId();

    default T open(@NotNull Player player) {
        return open(player, false);
    }

    T open(@NotNull Player player, boolean reopen);

    default T close() {
        return close(true);
    }

    T close(boolean closePacket);

    Consumer<GlitchInventoryOpenEvent<T>> openAction();

    T openAction(@Nullable Consumer<GlitchInventoryOpenEvent<T>> openAction);

    Consumer<GlitchInventoryCloseEvent<T>> closeAction();

    T closeAction(@Nullable Consumer<GlitchInventoryCloseEvent<T>> closeAction);

    ClickAction<T> clickAction();

    T clickAction(@Nullable ClickAction<T> clickAction);

    void handleClick(Integer containerId, int slot, @NotNull ClickType clickType);
}
