package me.glicz.inventoryapi.util.builder.pattern;

import me.glicz.inventoryapi.GlitchInventory;
import me.glicz.inventoryapi.item.builder.ItemBuilder;
import me.glicz.inventoryapi.item.gui.GuiItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public interface InventoryPatternBuilder<T extends GlitchInventory<T>> {
    @Contract(value = "_ -> new", pure = true)
    static <T extends GlitchInventory<T>> @NotNull InventoryPatternBuilder<T> create(T inventory) {
        return new InventoryPatternBuilderImpl<>(inventory);
    }

    @Unmodifiable List<String> pattern();

    InventoryPatternBuilder<T> pattern(String... pattern);

    GuiItem replacement(char character);

    default InventoryPatternBuilder<T> replacement(char character, @NotNull ItemStack itemStack) {
        return replacement(character, GuiItem.guiItem(itemStack));
    }

    default InventoryPatternBuilder<T> replacement(char character, @NotNull ItemBuilder itemBuilder) {
        return replacement(character, itemBuilder.asGuiItem());
    }

    InventoryPatternBuilder<T> replacement(char character, @Nullable GuiItem guiItem);

    T build();
}
