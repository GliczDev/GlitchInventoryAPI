package me.glicz.inventoryapi.title;

import me.glicz.inventoryapi.GlitchInventory;
import net.kyori.adventure.text.Component;

public class SimpleTitle extends Title<SimpleTitle> {
    private final Component component;

    protected SimpleTitle(Component component) {
        this.component = component;
    }

    @Override
    public Component component() {
        return component;
    }

    @Override
    public SimpleTitle apply(GlitchInventory<?> glitchInventory) {
        return this;
    }
}
