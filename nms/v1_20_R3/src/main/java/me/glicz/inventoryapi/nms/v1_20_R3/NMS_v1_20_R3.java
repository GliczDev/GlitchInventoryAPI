package me.glicz.inventoryapi.nms.v1_20_R3;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.papermc.paper.adventure.PaperAdventure;
import me.glicz.inventoryapi.GlitchInventory;
import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.MerchantGlitchInventory;
import me.glicz.inventoryapi.click.ClickType;
import me.glicz.inventoryapi.item.gui.GuiItem;
import me.glicz.inventoryapi.nms.NMS;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NMS_v1_20_R3 implements NMS {
    protected ServerPlayer serverPlayer(Player player) {
        return MinecraftServer.getServer().getPlayerList().getPlayer(player.getUniqueId());
    }

    protected MerchantOffer merchantOffer(MerchantRecipe recipe) {
        return new MerchantOffer(
                !recipe.getIngredients().isEmpty()
                        ? ItemStack.fromBukkitCopy(recipe.getIngredients().get(0))
                        : ItemStack.EMPTY,
                recipe.getIngredients().size() > 1
                        ? ItemStack.fromBukkitCopy(recipe.getIngredients().get(1))
                        : ItemStack.EMPTY,
                ItemStack.fromBukkitCopy(recipe.getResult()),
                recipe.getUses(),
                recipe.getMaxUses(),
                recipe.getVillagerExperience(),
                recipe.getPriceMultiplier(),
                0,
                recipe.shouldIgnoreDiscounts()
        );
    }

    @Override
    public void injectPlayer(Player player) {
        serverPlayer(player).connection.connection.channel.pipeline().addBefore(
                "packet_handler",
                GlitchInventoryAPI.get().plugin().getName() + "_GlitchInventoryAPI_Handler",
                new PacketHandler(player)
        );
    }

    @Override
    public void uninjectPlayer(Player player) {
        Channel channel = serverPlayer(player).connection.connection.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(GlitchInventoryAPI.get().plugin().getName() + "_GlitchInventoryAPI_Handler");
            return null;
        });
    }

    protected void handleS2CPacket(Player player, Object msg) {
        GlitchInventory<?> inventory = GlitchInventory.get(player);
        if (inventory == null || inventory.containerId() == null) return;

        if (msg instanceof ClientboundContainerClosePacket) {
            inventory.close(false);
        }
    }

    protected boolean handleC2SPacket(Player player, Object msg) {
        GlitchInventory<?> inventory = GlitchInventory.get(player);
        if (inventory == null) return false;

        if (msg instanceof ServerboundContainerClickPacket packet) {
            handlePacketAction(() -> inventory.handleClick(
                    inventory.containerId(),
                    packet.getSlotNum(),
                    ClickType.get(packet.getClickType().ordinal(), packet.getButtonNum())
            ));
            return true;
        } else if (msg instanceof ServerboundContainerClosePacket) {
            handlePacketAction(() -> inventory.close(false));
            return true;
        } else if (inventory instanceof MerchantGlitchInventory merchantInventory && msg instanceof ServerboundSelectTradePacket packet) {
            handlePacketAction(() -> merchantInventory.handleRecipeSelect(packet.getItem()));
            return true;
        } else {
            return msg instanceof ServerboundSwingPacket;
        }
    }

    @Override
    public int nextContainerId(Player player) {
        return serverPlayer(player).nextContainerCounter();
    }

    protected MenuType<?> menuType(int rows) {
        return switch (rows) {
            case 1 -> MenuType.GENERIC_9x1;
            case 2 -> MenuType.GENERIC_9x2;
            case 3 -> MenuType.GENERIC_9x3;
            case 4 -> MenuType.GENERIC_9x4;
            case 5 -> MenuType.GENERIC_9x5;
            case 6 -> MenuType.GENERIC_9x6;
            default -> throw new IllegalArgumentException("Can't open a %s rows inventory!".formatted(rows));
        };
    }

    @SuppressWarnings("removal")
    protected MenuType<?> menuType(InventoryType inventoryType) {
        return switch (inventoryType) {
            case PLAYER -> MenuType.GENERIC_9x4;
            case WORKBENCH -> MenuType.CRAFTING;
            case FURNACE -> MenuType.FURNACE;
            case DISPENSER, DROPPER -> MenuType.GENERIC_3x3;
            case ENCHANTING -> MenuType.ENCHANTMENT;
            case BREWING -> MenuType.BREWING_STAND;
            case BEACON -> MenuType.BEACON;
            case ANVIL -> MenuType.ANVIL;
            case SMITHING, SMITHING_NEW -> MenuType.SMITHING;
            case HOPPER -> MenuType.HOPPER;
            case SHULKER_BOX -> MenuType.SHULKER_BOX;
            case BLAST_FURNACE -> MenuType.BLAST_FURNACE;
            case LECTERN -> MenuType.LECTERN;
            case SMOKER -> MenuType.SMOKER;
            case LOOM -> MenuType.LOOM;
            case CARTOGRAPHY -> MenuType.CARTOGRAPHY_TABLE;
            case GRINDSTONE -> MenuType.GRINDSTONE;
            case STONECUTTER -> MenuType.STONECUTTER;
            case MERCHANT -> MenuType.MERCHANT;
            default -> {
                if (!inventoryType.isCreatable()) {
                    throw new IllegalArgumentException("Can't create a %s inventory!".formatted(inventoryType));
                }
                yield MenuType.GENERIC_9x3;
            }
        };
    }

    @Override
    public void openInventory(GlitchInventory<?> inventory) {
        serverPlayer(inventory.viewer()).connection.send(new ClientboundOpenScreenPacket(
                inventory.containerId(),
                inventory.inventoryType() == InventoryType.CHEST
                        ? menuType(inventory.size() / 9)
                        : menuType(inventory.inventoryType()),
                PaperAdventure.asVanilla(inventory.title().component())
        ));
    }

    @Override
    public void closeInventory(GlitchInventory<?> inventory) {
        serverPlayer(inventory.viewer()).connection.send(new ClientboundContainerClosePacket(
                inventory.containerId()
        ));
    }

    @Override
    public void sendItems(GlitchInventory<?> inventory) {
        sendItems0(inventory.containerId(), inventory.viewer(), inventory.allItems().stream().map(GuiItem::itemStack).toList());
    }

    protected void sendItems0(int id, Player player, List<org.bukkit.inventory.ItemStack> items) {
        ItemStack[] itemStacks = items.stream()
                .map(ItemStack::fromBukkitCopy)
                .toArray(ItemStack[]::new);
        serverPlayer(player).connection.send(new ClientboundContainerSetContentPacket(
                id, 0,
                NonNullList.of(ItemStack.EMPTY, itemStacks),
                ItemStack.EMPTY
        ));
    }

    @Override
    public void sendItem(GlitchInventory<?> inventory, int slot) {
        serverPlayer(inventory.viewer()).connection.send(new ClientboundContainerSetSlotPacket(
                inventory.containerId(), 0, slot,
                ItemStack.fromBukkitCopy(inventory.item(slot).itemStack())
        ));
    }

    @Override
    public void sendRecipes(MerchantGlitchInventory inventory) {
        MerchantOffers offers = new MerchantOffers();
        inventory.recipes().forEach(recipe -> offers.add(merchantOffer(recipe)));
        serverPlayer(inventory.viewer()).connection.send(new ClientboundMerchantOffersPacket(
                inventory.containerId(), offers,
                0, 0,
                false, false
        ));
    }

    @Override
    public void verifyInventoryType(int rows) {
        menuType(rows); // Throws an exception if invalid
    }

    @Override
    public void verifyInventoryType(InventoryType inventoryType) {
        menuType(inventoryType); // Throws an exception if invalid
    }

    class PacketHandler extends ChannelDuplexHandler {
        private final Player player;

        public PacketHandler(Player player) {
            this.player = player;
        }

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            try {
                handleS2CPacket(player, msg);
            } catch (Exception ex) {
                GlitchInventoryAPI.get().plugin().getSLF4JLogger().atError()
                        .setCause(ex)
                        .log("An exception occurred while handling S2C packet in GlitchInventoryAPI");
            }
            super.write(ctx, msg, promise);
        }

        @Override
        public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) throws Exception {
            try {
                if (handleC2SPacket(player, msg)) {
                    return;
                }
            } catch (Exception ex) {
                GlitchInventoryAPI.get().plugin().getSLF4JLogger().atError()
                        .setCause(ex)
                        .log("An exception occurred while handling C2S packet in GlitchInventoryAPI");
            }
            super.channelRead(ctx, msg);
        }
    }
}
