package me.glicz.inventoryapi.nms;

import me.glicz.inventoryapi.GlitchInventory;
import me.glicz.inventoryapi.MerchantGlitchInventory;
import me.glicz.inventoryapi.click.ClickType;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.item.ItemStack;
import org.bukkit.entity.Player;

import java.util.List;

public class NMSBridge_v1_21_5 extends NMSBridge_v1_20_6 {
    @Override
    protected boolean handleC2SPacket(Player player, Object msg) {
        GlitchInventory<?> inventory = GlitchInventory.get(player);
        if (inventory == null) return false;

        return switch (msg) {
            case ServerboundContainerClickPacket packet -> {
                handlePacketAction(() -> inventory.handleClick(
                        player,
                        inventory.containerId(player),
                        packet.slotNum(),
                        ClickType.get(packet.clickType().ordinal(), packet.buttonNum())
                ));
                yield true;
            }
            case ServerboundContainerClosePacket $ -> {
                handlePacketAction(() -> inventory.close(player, false));
                yield true;
            }
            case ServerboundSelectTradePacket packet when inventory instanceof MerchantGlitchInventory merchantInventory -> {
                handlePacketAction(() -> merchantInventory.handleRecipeSelect(player, packet.getItem()));
                yield true;
            }
            case ServerboundSwingPacket $ -> true;
            default -> false;
        };
    }

    @Override
    protected void sendItems0(Player player, int id, List<org.bukkit.inventory.ItemStack> items) {
        List<ItemStack> itemStacks = items.stream()
                .map(ItemStack::fromBukkitCopy)
                .toList();

        serverPlayer(player).connection.send(new ClientboundContainerSetContentPacket(
                id, 0, itemStacks, ItemStack.EMPTY
        ));
    }
}
