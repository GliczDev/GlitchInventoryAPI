package me.glicz.inventoryapi.item.builder;

import me.glicz.inventoryapi.GlitchInventory;
import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.click.ClickAction;
import me.glicz.inventoryapi.item.gui.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
abstract sealed class AbstractItemBuilder<S extends AbstractItemBuilder<S, M>, M extends ItemMeta> permits ItemBuilder, LeatherArmorBuilder, SkullBuilder {
    protected final ItemStack itemStack;
    protected final M itemMeta;

    AbstractItemBuilder(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack.clone();

        try {
            this.itemMeta = (M) this.itemStack.getItemMeta();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException();
        }
    }

    public @NotNull ItemStack asItemStack() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public @NotNull GuiItem asGuiItem() {
        return asGuiItem(null);
    }

    public @NotNull <T extends GlitchInventory<T>> GuiItem asGuiItem(@Nullable ClickAction<T> clickAction) {
        return GuiItem.guiItem(asItemStack(), clickAction);
    }

    public int amount() {
        return itemStack.getAmount();
    }

    public S amount(int amount) {
        itemStack.setAmount(amount);
        return (S) this;
    }

    public Component name() {
        return itemMeta.displayName();
    }

    public S name(@Nullable Component name) {
        itemMeta.itemName(name);
        return (S) this;
    }

    public List<Component> lore() {
        return itemMeta.lore();
    }

    public S lore(@Nullable List<Component> lore) {
        itemMeta.lore(lore);
        return (S) this;
    }

    public Integer customModelData() {
        return itemMeta.hasCustomModelData() ? itemMeta.getCustomModelData() : null;
    }

    public S customModelData(@Nullable Integer customModelData) {
        itemMeta.setCustomModelData(customModelData);
        return (S) this;
    }

    public @NotNull Set<ItemFlag> flags() {
        return itemMeta.getItemFlags();
    }

    public S addFlags(@NotNull ItemFlag... flags) {
        itemMeta.addItemFlags(flags);
        return (S) this;
    }

    public S removeFlags(@NotNull ItemFlag... flags) {
        itemMeta.removeItemFlags(flags);
        return (S) this;
    }

    public @NotNull Map<Enchantment, Integer> enchants() {
        return itemMeta.getEnchants();
    }

    public S addEnchant(@NotNull Enchantment enchantment, int level) {
        return addEnchant(enchantment, level, false);
    }

    public S addEnchant(@NotNull Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        itemMeta.addEnchant(enchantment, level, ignoreLevelRestriction);
        return (S) this;
    }

    public <D, Z> S setPDCValue(String key, PersistentDataType<D, Z> valueType, Z value) {
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(GlitchInventoryAPI.get().plugin(), key), valueType, value);
        return (S) this;
    }

    public <D, Z> Z getPDCValue(String key, PersistentDataType<D, Z> valueType) {
        return itemMeta.getPersistentDataContainer().get(new NamespacedKey(GlitchInventoryAPI.get().plugin(), key), valueType);
    }
}
