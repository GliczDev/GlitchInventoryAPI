package me.glicz.inventoryapi.factory;

import me.glicz.inventoryapi.GlitchInventory;
import org.bukkit.event.inventory.InventoryType;

public class InvalidInventoryType extends RuntimeException {
    public InvalidInventoryType(Class<? extends GlitchInventory<?>> clazz, int rows) {
        super(rows + "rows are not supported by " + clazz.getSimpleName());
    }

    public InvalidInventoryType(Class<? extends GlitchInventory<?>> clazz, InventoryType type) {
        super(type.name() + " is not supported by " + clazz.getSimpleName());
    }
}