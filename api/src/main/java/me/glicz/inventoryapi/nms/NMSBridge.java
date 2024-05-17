package me.glicz.inventoryapi.nms;

import me.glicz.inventoryapi.GlitchInventory;
import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.MerchantGlitchInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface NMSBridge {
    void injectPlayer(Player player);

    void uninjectPlayer(Player player);

    int nextContainerId(Player player);

    void openInventory(Player player, GlitchInventory<?> inventory);

    void closeInventory(Player player, GlitchInventory<?> inventory);

    void sendItems(Player player, GlitchInventory<?> inventory);

    void sendItem(Player player, GlitchInventory<?> inventory, int slot);

    void sendRecipes(Player player, MerchantGlitchInventory inventory);

    void verifyInventoryType(int rows);

    void verifyInventoryType(InventoryType inventoryType);

    default void handlePacketAction(Runnable action) {
        if (GlitchInventoryAPI.get().config().syncPacketHandlers()) {
            Bukkit.getScheduler().runTask(GlitchInventoryAPI.get().plugin(), action);
        } else {
            action.run();
        }
    }
}
