package me.glicz.inventoryapi;

import org.bukkit.event.inventory.InventoryType;

public class SimpleGlitchInventoryImpl extends GlitchInventoryImpl<SimpleGlitchInventory> implements SimpleGlitchInventory {
    public SimpleGlitchInventoryImpl(int rows) {
        super(rows);
    }

    public SimpleGlitchInventoryImpl(InventoryType inventoryType) {
        super(inventoryType);
    }
}
