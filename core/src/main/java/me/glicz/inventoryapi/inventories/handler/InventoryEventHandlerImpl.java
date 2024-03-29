package me.glicz.inventoryapi.inventories.handler;

import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.events.InventoryClickEvent;
import me.glicz.inventoryapi.events.merchant.InventoryTradeSelectEvent;
import me.glicz.inventoryapi.inventories.ClickType;
import me.glicz.inventoryapi.inventories.GlitchInventory;
import me.glicz.inventoryapi.inventories.MerchantInventory;
import me.glicz.inventoryapi.itembuilders.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InventoryEventHandlerImpl extends InventoryEventHandler {

    @Override
    public void handleClick(Player player, int inventoryId, ClickType clickType, int slot) {
        if (GlitchInventory.has(player)) {
            GlitchInventory<?> inventory = GlitchInventory.get(player);
            inventory.updateItems(player);
            player.updateInventory();
            if (inventory.getId(player) != inventoryId) return;
            Runnable action = () -> {
                InventoryClickEvent event = new InventoryClickEvent(player, inventory, clickType, slot);
                inventory.executeSlotClickAction(slot, event);
                inventory.getItem(player, slot).executeClickAction(event);
            };
            if (GlitchInventoryAPI.getConfig().synchronizeHandlingPackets())
                Bukkit.getScheduler().runTask(GlitchInventoryAPI.getPlugin(), action);
            else
                action.run();
        }
    }

    @Override
    public void handleClose(Player player, int inventoryId) {
        if (GlitchInventory.has(player)) {
            GlitchInventory<?> inventory = GlitchInventory.get(player);
            if (GlitchInventoryAPI.getConfig().verifyInventoryIdOnClose() && inventory.getId(player) != inventoryId)
                return;
            if (GlitchInventoryAPI.getConfig().synchronizeHandlingPackets())
                Bukkit.getScheduler().runTask(GlitchInventoryAPI.getPlugin(), () -> inventory.silentClose(player));
            else
                inventory.silentClose(player);
        }
    }

    @Override
    public void handleItemRename(Player player, String name) {
        if (GlitchInventory.has(player))
            GlitchInventory.get(player).setItem(2, ItemBuilder.of(
                    GlitchInventory.get(player).getItem(0).getItemStack()).setName(name).asGuiItem());
    }

    @Override
    public void handleSelectTrade(Player player, int recipe) {
        if (GlitchInventory.get(player) instanceof MerchantInventory inventory) {
            inventory.setSelectedRecipe(player, recipe);
            InventoryTradeSelectEvent event = new InventoryTradeSelectEvent(player, inventory, recipe);
            if (GlitchInventoryAPI.getConfig().synchronizeHandlingPackets())
                Bukkit.getScheduler().runTask(GlitchInventoryAPI.getPlugin(), () -> inventory.executeTradeSelectAction(event));
            else
                inventory.executeTradeSelectAction(event);
        }
    }
}
