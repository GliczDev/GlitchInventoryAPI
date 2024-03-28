package me.glicz.inventoryapi;

import me.glicz.inventoryapi.config.GlitchInventoryAPIConfig;
import me.glicz.inventoryapi.factory.GlitchInventoryFactory;
import me.glicz.inventoryapi.nms.NMS;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.NonExtendable
public abstract class GlitchInventoryAPI {
    private static GlitchInventoryAPI glitchInventoryAPI;

    public static GlitchInventoryAPI get() {
        return glitchInventoryAPI;
    }

    public static void init(GlitchInventoryAPI glitchInventoryAPI) {
        if (GlitchInventoryAPI.glitchInventoryAPI != null) {
            throw new RuntimeException();
        }
        GlitchInventoryAPI.glitchInventoryAPI = glitchInventoryAPI;
        GlitchInventoryAPI.glitchInventoryAPI.init();
    }

    protected abstract void init();

    public abstract GlitchInventoryAPIConfig config();

    public abstract GlitchInventoryFactory factory();

    public JavaPlugin plugin() {
        return config().plugin();
    }

    abstract GlitchInventory<?> getGlitchInventory(@NotNull Player player);

    @ApiStatus.Internal
    public abstract NMS getNms();
}
