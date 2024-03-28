package me.glicz.inventoryapi.click;

import me.glicz.inventoryapi.GlitchInventory;
import me.glicz.inventoryapi.event.GlitchInventoryClickEvent;

import java.util.function.Consumer;

@FunctionalInterface
public interface ClickAction<T extends GlitchInventory<T>> extends Consumer<GlitchInventoryClickEvent<T>> {
}
