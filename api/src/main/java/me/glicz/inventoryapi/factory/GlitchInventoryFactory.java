package me.glicz.inventoryapi.factory;

import me.glicz.inventoryapi.GlitchInventory;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;

@ApiStatus.NonExtendable
public interface GlitchInventoryFactory {
    @Contract(value = "_ -> new", pure = true)
    <T extends GlitchInventory<T>> T newInventory(Class<T> clazz) throws InvalidInventoryType;

    @Contract(value = "_, _ -> new", pure = true)
    <T extends GlitchInventory<T>> T newInventory(Class<T> clazz, @Range(from = 1, to = 6) int rows) throws InvalidInventoryType;

    @Contract(value = "_, _ -> new", pure = true)
    <T extends GlitchInventory<T>> T newInventory(Class<T> clazz, InventoryType inventoryType) throws InvalidInventoryType;
}
