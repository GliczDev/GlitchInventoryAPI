package me.glicz.inventoryapi;

import lombok.Getter;
import lombok.experimental.Accessors;
import me.glicz.inventoryapi.config.GlitchInventoryAPIConfig;
import me.glicz.inventoryapi.factory.GlitchInventoryFactory;
import me.glicz.inventoryapi.factory.GlitchInventoryFactoryImpl;
import me.glicz.inventoryapi.listener.JoinQuitListener;
import me.glicz.inventoryapi.nms.NMSBridge;
import me.glicz.inventoryapi.nms.NMSBridge_v1_20_6;
import me.glicz.inventoryapi.nms.NMSBridge_v1_21_5;
import me.glicz.inventoryapi.util.MinecraftVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
public class GlitchInventoryAPIImpl extends GlitchInventoryAPI {
    private final GlitchInventoryAPIConfig config;
    private final NMSBridge nmsBridge;
    private GlitchInventoryFactory factory;

    public GlitchInventoryAPIImpl(@NotNull GlitchInventoryAPIConfig config) {
        this.config = config;
        this.nmsBridge = resolveNMSBridge();
    }

    private static NMSBridge resolveNMSBridge() {
        MinecraftVersion version = MinecraftVersion.currentVersion();

        if (version.isAtLeast(1, 21, 5)) {
            return new NMSBridge_v1_21_5();
        } else if (version.isAtLeast(1, 20, 6)) {
            return new NMSBridge_v1_20_6();
        }

        throw new IllegalStateException("Unsupported version: " + version);
    }

    @Override
    protected void init() {
        factory = new GlitchInventoryFactoryImpl();

        Bukkit.getPluginManager().registerEvents(new JoinQuitListener(), plugin());
    }

    @Override
    GlitchInventory<?> getGlitchInventory(@NotNull Player player) {
        return GlitchInventoryImpl.get(player);
    }
}
