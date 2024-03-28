package me.glicz.inventoryapi.event.merchant;

import me.glicz.inventoryapi.MerchantGlitchInventory;
import me.glicz.inventoryapi.event.GlitchInventoryEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;

public class GlitchInventoryRecipeSelectEvent extends GlitchInventoryEvent<MerchantGlitchInventory> {
    private final int index;
    private final MerchantRecipe recipe;

    public GlitchInventoryRecipeSelectEvent(Player player, MerchantGlitchInventory glitchInventory, int index) {
        super(player, glitchInventory);
        this.index = index;
        this.recipe = glitchInventory.recipe(index);
    }

    public int recipeIndex() {
        return index;
    }

    public @NotNull MerchantRecipe recipe() {
        return recipe;
    }
}
