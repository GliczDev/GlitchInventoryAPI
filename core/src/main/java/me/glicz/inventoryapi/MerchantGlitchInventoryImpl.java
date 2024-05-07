package me.glicz.inventoryapi;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import me.glicz.inventoryapi.event.merchant.GlitchInventoryRecipeSelectEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Getter
@Accessors(fluent = true)
public class MerchantGlitchInventoryImpl extends GlitchInventoryImpl<MerchantGlitchInventory> implements MerchantGlitchInventory {
    private List<MerchantRecipe> recipes = new ArrayList<>();
    @Getter(AccessLevel.NONE)
    private int selectedRecipe;
    private Consumer<GlitchInventoryRecipeSelectEvent> recipeSelectAction;

    public MerchantGlitchInventoryImpl() {
        super(InventoryType.MERCHANT);
    }

    @Override
    public MerchantGlitchInventory open(@NotNull Player player, boolean reuseCurrent) {
        super.open(player, reuseCurrent);

        if (recipe(0) != null) {
            if (recipeSelectAction != null) {
                recipeSelectAction.accept(new GlitchInventoryRecipeSelectEvent(player, this, 0));
            }
            this.selectedRecipe = 0;
        } else {
            this.selectedRecipe = -1;
        }

        return this;
    }

    @Override
    public int selectedRecipeIndex() {
        return selectedRecipe;
    }

    @Override
    public @NotNull List<@NotNull MerchantRecipe> recipes() {
        return List.copyOf(recipes);
    }

    @Override
    public MerchantGlitchInventory recipes(@Nullable List<@NotNull MerchantRecipe> recipes) {
        if (recipes == null) {
            this.recipes = new ArrayList<>();
        } else {
            this.recipes = new ArrayList<>(recipes);
        }

        if (recipe(0) != null) {
            if (recipeSelectAction != null) {
                recipeSelectAction.accept(new GlitchInventoryRecipeSelectEvent(viewer(), this, 0));
            }
            this.selectedRecipe = 0;
        } else {
            this.selectedRecipe = -1;
        }

        if (viewer() != null) {
            GlitchInventoryAPI.get().nmsBridge().sendRecipes(this);
        }

        return this;
    }

    @Override
    public MerchantGlitchInventory recipeSelectAction(@Nullable Consumer<GlitchInventoryRecipeSelectEvent> recipeSelectAction) {
        this.recipeSelectAction = recipeSelectAction;
        return this;
    }

    @Override
    public void handleRecipeSelect(int index) {
        Preconditions.checkArgument(index < recipes.size(), "slot >= recipes.size()");

        updateItems();
        if (!modifyPlayerInventory()) {
            viewer().updateInventory();
        }

        if (recipeSelectAction != null) {
            recipeSelectAction.accept(new GlitchInventoryRecipeSelectEvent(viewer(), this, index));
        }
        this.selectedRecipe = index;
    }

    @Override
    protected void openInventory() {
        if (viewer() != null) {
            super.openInventory();
            GlitchInventoryAPI.get().nmsBridge().sendRecipes(this);
        }
    }
}
