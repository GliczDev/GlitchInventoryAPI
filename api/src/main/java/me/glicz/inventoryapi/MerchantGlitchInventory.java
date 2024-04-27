package me.glicz.inventoryapi;

import me.glicz.inventoryapi.event.merchant.GlitchInventoryRecipeSelectEvent;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.List;
import java.util.function.Consumer;

public interface MerchantGlitchInventory extends GlitchInventory<MerchantGlitchInventory> {
    default MerchantRecipe recipe(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        try {
            return recipes().get(index);
        } catch (IndexOutOfBoundsException ignored) {
            return null;
        }
    }

    int selectedRecipeIndex();

    default MerchantRecipe selectedRecipe() {
        return recipe(selectedRecipeIndex());
    }

    @NotNull
    List<@NotNull MerchantRecipe> recipes();

    MerchantGlitchInventory recipes(@Nullable List<@NotNull MerchantRecipe> recipes);

    Consumer<GlitchInventoryRecipeSelectEvent> recipeSelectAction();

    MerchantGlitchInventory recipeSelectAction(@Nullable Consumer<GlitchInventoryRecipeSelectEvent> recipeSelectAction);

    void handleRecipeSelect(int index);
}
