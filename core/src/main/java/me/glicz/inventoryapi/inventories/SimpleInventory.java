package me.glicz.inventoryapi.inventories;

import org.bukkit.event.inventory.InventoryType;

public class SimpleInventory extends GlitchInventory<SimpleInventory> {

    protected SimpleInventory(InventoryType inventoryType) {
        super(inventoryType);
    }

    protected SimpleInventory(int rows) {
        super(rows);
    }
}
