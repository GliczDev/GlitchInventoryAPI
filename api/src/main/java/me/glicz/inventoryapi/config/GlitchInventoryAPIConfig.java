package me.glicz.inventoryapi.config;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class GlitchInventoryAPIConfig {
    private final JavaPlugin plugin;
    private boolean useLatestNms = false;
    private boolean syncPacketHandlers = true;

    private GlitchInventoryAPIConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull GlitchInventoryAPIConfig create(JavaPlugin plugin) {
        return new GlitchInventoryAPIConfig(plugin);
    }

    public JavaPlugin plugin() {
        return plugin;
    }

    public boolean useLatestNms() {
        return useLatestNms;
    }

    public void useLatestNms(boolean useLatestNms) {
        this.useLatestNms = useLatestNms;
    }

    public boolean syncPacketHandlers() {
        return syncPacketHandlers;
    }

    public void syncPacketHandlers(boolean syncPacketHandlers) {
        this.syncPacketHandlers = syncPacketHandlers;
    }
}
