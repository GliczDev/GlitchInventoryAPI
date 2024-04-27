package me.glicz.inventoryapi;

import lombok.Getter;
import lombok.experimental.Accessors;
import me.glicz.inventoryapi.config.GlitchInventoryAPIConfig;
import me.glicz.inventoryapi.factory.GlitchInventoryFactory;
import me.glicz.inventoryapi.factory.GlitchInventoryFactoryImpl;
import me.glicz.inventoryapi.listener.JoinQuitListener;
import me.glicz.inventoryapi.nms.NMSBridge;
import me.glicz.inventoryapi.nms.NMSBridgeImpl;
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
        this.nmsBridge = new NMSBridgeImpl();
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
