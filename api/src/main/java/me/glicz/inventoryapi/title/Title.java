package me.glicz.inventoryapi.title;

import me.glicz.inventoryapi.GlitchInventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;

import java.util.function.Function;

public abstract class Title<T extends Title<T>> implements Function<GlitchInventory<?>, T> {
    public static SimpleTitle simple(String title) {
        return simple(Component.text(title));
    }

    public static SimpleTitle simple(ComponentLike title) {
        return new SimpleTitle(title.asComponent());
    }

    public abstract Component component();
}