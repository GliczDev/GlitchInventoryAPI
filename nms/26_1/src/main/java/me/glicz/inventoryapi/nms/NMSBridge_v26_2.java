package me.glicz.inventoryapi.nms;

import me.glicz.inventoryapi.GlitchInventory;
import me.glicz.inventoryapi.MerchantGlitchInventory;
import me.glicz.inventoryapi.click.ClickType;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import org.bukkit.entity.Player;

public class NMSBridge_v26_2 extends NMSBridge_v1_21_5 {
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
                        ClickType.get(packet.containerInput().ordinal(), packet.buttonNum())
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
}
