package me.glicz.inventoryapi.util.builder.pattern;

import me.glicz.inventoryapi.GlitchInventory;
import me.glicz.inventoryapi.item.gui.GuiItem;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InventoryPatternBuilderImpl<T extends GlitchInventory<T>> implements InventoryPatternBuilder<T> {
    private final Map<Character, GuiItem> replacementMap = new HashMap<>();
    private final T inventory;
    private List<String> pattern;

    InventoryPatternBuilderImpl(T inventory) {
        this.inventory = inventory;
    }

    @Override
    public @Unmodifiable List<String> pattern() {
        return Collections.unmodifiableList(pattern);
    }

    @Override
    public InventoryPatternBuilder<T> pattern(String... pattern) {
        if (String.join("", pattern).length() != inventory.size()) {
            throw new RuntimeException("Pattern length must be the same as inventory size!");
        }

        this.pattern = List.of(pattern);
        return this;
    }

    @Override
    public GuiItem replacement(char character) {
        return replacementMap.get(character);
    }

    @Override
    public InventoryPatternBuilder<T> replacement(char character, @Nullable GuiItem guiItem) {
        replacementMap.put(character, guiItem);
        return this;
    }

    @Override
    public T build() {
        for (int y = 0; y < pattern.size(); y++) {
            String line = pattern.get(y);
            char[] chars = line.toCharArray();
            for (int x = 0; x < chars.length; x++) {
                char id = chars[x];

                if (id == ' ') {
                    continue;
                }

                if (!replacementMap.containsKey(id)) {
                    throw new RuntimeException("No replacement found for key '%s'!".formatted(id));
                }

                inventory.item(y * line.length() + x, replacementMap.get(id));
            }
        }

        return inventory;
    }
}
