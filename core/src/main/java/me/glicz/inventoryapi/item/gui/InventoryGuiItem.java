package me.glicz.inventoryapi.item.gui;

import me.glicz.inventoryapi.GlitchInventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InventoryGuiItem<T extends GlitchInventory<T>> extends SimpleGuiItem {
    private final T inventory;
    private final int slot;

    public InventoryGuiItem(T inventory, int slot, ItemStack itemStack) {
        super(itemStack, null);
        this.inventory = inventory;
        this.slot = slot;
    }

    public InventoryGuiItem(T inventory, int slot, GuiItem guiItem) {
        this(inventory, slot, guiItem.itemStack());
        clickAction = guiItem.clickAction();
    }

    @Override
    public void itemStack(@NotNull ItemStack itemStack) {
        super.itemStack(itemStack);

        if (inventory == null) return;

        if (slot == -1) {
            /*
            Well, I currently don't a have better idea on how to handle pagination items in PaginatedGlitchInventory
            I would need to handle page changes and change slots of these items etc.
            Imo too much effort in such a small feature - just updating the whole inventory is fine
             */
            inventory.updateItems();
        } else if (inventory.item(slot) == this) {
            inventory.updateItem(slot);
        }
    }
}
