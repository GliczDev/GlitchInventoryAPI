package me.glicz.inventoryapi;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.experimental.Accessors;
import me.glicz.inventoryapi.click.ClickAction;
import me.glicz.inventoryapi.click.ClickType;
import me.glicz.inventoryapi.event.GlitchInventoryClickEvent;
import me.glicz.inventoryapi.event.GlitchInventoryCloseEvent;
import me.glicz.inventoryapi.event.GlitchInventoryOpenEvent;
import me.glicz.inventoryapi.item.gui.GuiItem;
import me.glicz.inventoryapi.item.gui.InventoryGuiItem;
import me.glicz.inventoryapi.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Getter
@Accessors(fluent = true)
public class GlitchInventoryImpl<T extends GlitchInventory<T>> implements GlitchInventory<T> {
    private static final Map<UUID, GlitchInventoryImpl<?>> GLITCH_INVENTORY_MAP = new HashMap<>();
    private final Map<UUID, Integer> viewerMap = new HashMap<>();
    private final int size;
    private final InventoryType inventoryType;
    private final GuiItem[] items;
    private boolean modifyPlayerInventory;
    private GuiItem[] extraItems = new GuiItem[0];
    private Title<?> title;
    private Consumer<GlitchInventoryOpenEvent<T>> openAction;
    private Consumer<GlitchInventoryCloseEvent<T>> closeAction;
    private ClickAction<T> clickAction;

    protected GlitchInventoryImpl(int rows) {
        this(rows * 9, InventoryType.CHEST);
        GlitchInventoryAPI.get().nmsBridge().verifyInventoryType(rows);
    }

    protected GlitchInventoryImpl(InventoryType inventoryType) {
        this(inventoryType.getDefaultSize(), inventoryType);
        GlitchInventoryAPI.get().nmsBridge().verifyInventoryType(inventoryType);
    }

    protected GlitchInventoryImpl(int size, InventoryType inventoryType) {
        this.size = size;
        this.inventoryType = inventoryType;

        this.items = new GuiItem[size];
        for (int i = 0; i < items.length; i++) {
            items[i] = new InventoryGuiItem<>((T) this, i, new ItemStack(Material.AIR));
        }

        this.title = Title.simple(inventoryType.defaultTitle());
    }

    static GlitchInventoryImpl<?> get(@NotNull Player player) {
        return GLITCH_INVENTORY_MAP.get(player.getUniqueId());
    }

    @Override
    public @NotNull List<@NotNull GuiItem> items() {
        return List.of(items);
    }

    @Override
    public T item(@Range(from = 0, to = Integer.MAX_VALUE) int slot, @Nullable GuiItem guiItem) {
        Preconditions.checkArgument(slot >= 0, "slot < 0");

        if (slot > size && modifyPlayerInventory) {
            extraItem(slot - size, guiItem);
            return (T) this;
        }

        Preconditions.checkArgument(slot < size, "slot >= size");

        if (guiItem == null) {
            items[slot] = new InventoryGuiItem<>((T) this, slot, new ItemStack(Material.AIR));
        } else {
            items[slot] = new InventoryGuiItem<>((T) this, slot, guiItem);
        }
        updateItem(slot);

        return (T) this;
    }

    protected void extraItem(int slot, GuiItem guiItem) {
        if (guiItem == null) {
            extraItems[slot] = new InventoryGuiItem<>((T) this, slot, new ItemStack(Material.AIR));
        } else {
            extraItems[slot] = new InventoryGuiItem<>((T) this, slot, guiItem);
        }
        updateItem(slot);
    }

    @Override
    public T modifyPlayerInventory(boolean modifyPlayerInventory) {
        if (this.modifyPlayerInventory == modifyPlayerInventory) return (T) this;
        this.modifyPlayerInventory = modifyPlayerInventory;

        this.extraItems = new GuiItem[modifyPlayerInventory ? InventoryType.PLAYER.getDefaultSize() : 0];
        for (int i = 0; i < extraItems.length; i++) {
            extraItems[i] = new InventoryGuiItem<>((T) this, i, new ItemStack(Material.AIR));
        }

        return (T) this;
    }

    @Override
    public @NotNull List<@NotNull GuiItem> extraItems() {
        return List.of(extraItems);
    }

    @Override
    public T updateItems(Player player) {
        GlitchInventoryAPI.get().nmsBridge().sendItems(player, this);
        return (T) this;
    }

    @Override
    public T updateItem(Player player, int slot) {
        GlitchInventoryAPI.get().nmsBridge().sendItem(player, this, slot);
        return (T) this;
    }

    @Override
    public T title(Title<?> title) {
        this.title = title;
        viewers().forEach(this::openInventory);
        return (T) this;
    }

    @Override
    public Set<Player> viewers() {
        return viewerMap.keySet().stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Integer containerId(Player player) {
        return viewerMap.get(player.getUniqueId());
    }

    @Override
    public T open(@NotNull Player player, boolean reuseCurrent) {
        if (viewerMap.containsKey(player.getUniqueId())) {
            openInventory(player);
            updateItems(player);
            return (T) this;
        }

        Integer containerId = null;
        boolean hasClosedBukkit = false;
        if (get(player) != null) {
            GlitchInventoryImpl<?> current = get(player);

            if (reuseCurrent) {
                containerId = current.containerId(player);
            }

            current.close(!reuseCurrent);
        } else if (player.getOpenInventory().getTopInventory() != player.getInventory()) {
            player.closeInventory();
            hasClosedBukkit = true;
        }

        if (containerId == null) {
            containerId = GlitchInventoryAPI.get().nmsBridge().nextContainerId(player);
        }

        int finalContainerId = containerId;
        Bukkit.getScheduler().runTaskLater(GlitchInventoryAPI.get().plugin(), () -> {
            GLITCH_INVENTORY_MAP.put(player.getUniqueId(), this);
            viewerMap.put(player.getUniqueId(), finalContainerId);

            openInventory(player);
            updateItems(player);

            if (openAction != null) openAction.accept(new GlitchInventoryOpenEvent<>(player, (T) this));
        }, hasClosedBukkit ? 2 : 1);
        return (T) this;
    }

    @Override
    public T close(Player player, boolean closePacket) {
        if (!viewerMap.containsKey(player.getUniqueId())) {
            return (T) this;
        }

        if (closeAction != null) closeAction.accept(new GlitchInventoryCloseEvent<>(player, (T) this));

        if (closePacket) GlitchInventoryAPI.get().nmsBridge().closeInventory(player, this);
        player.updateInventory();

        viewerMap.remove(player.getUniqueId());
        GLITCH_INVENTORY_MAP.remove(player.getUniqueId());

        return (T) this;
    }

    @Override
    public T openAction(@Nullable Consumer<GlitchInventoryOpenEvent<T>> openAction) {
        this.openAction = openAction;
        return (T) this;
    }

    @Override
    public T closeAction(@Nullable Consumer<GlitchInventoryCloseEvent<T>> closeAction) {
        this.closeAction = closeAction;
        return (T) this;
    }

    @Override
    public T clickAction(@Nullable ClickAction<T> clickAction) {
        this.clickAction = clickAction;
        return (T) this;
    }

    @Override
    public void handleClick(Player player, Integer containerId, int slot, @NotNull ClickType clickType) {
        if (containerId == null || (!(containerId == 0 && modifyPlayerInventory) && !containerId.equals(containerId(player)))) {
            return;
        }

        player.setItemOnCursor(null);
        updateItems(player);
        if (!modifyPlayerInventory) {
            player.updateInventory();
        }

        GlitchInventoryClickEvent<T> event = new GlitchInventoryClickEvent<>(player, (T) this, item(slot), slot, clickType);

        try {
            Optional.ofNullable(clickAction).ifPresent(clickAction -> clickAction.accept(event));
        } catch (Exception ex) {
            GlitchInventoryAPI.get().plugin().getSLF4JLogger().atError()
                    .setCause(ex)
                    .log("An exception occurred while executing default click action on slot {}", slot);
        }

        try {
            Optional.ofNullable(item(slot)).map(GuiItem::<T>clickAction).ifPresent(clickAction -> clickAction.accept(event));
        } catch (Exception ex) {
            GlitchInventoryAPI.get().plugin().getSLF4JLogger().atError()
                    .setCause(ex)
                    .log("An exception occurred while executing GuiItem click action on slot {}", slot);
        }
    }

    protected void openInventory(Player player) {
        GlitchInventoryAPI.get().nmsBridge().openInventory(player, this);
    }
}
