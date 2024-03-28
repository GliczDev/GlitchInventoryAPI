package me.glicz.inventoryapi.plugin;

import me.glicz.inventoryapi.GlitchInventoryAPI;
import me.glicz.inventoryapi.GlitchInventoryAPIImpl;
import me.glicz.inventoryapi.config.GlitchInventoryAPIConfig;
import org.bukkit.plugin.java.JavaPlugin;

public class GlitchInventoryAPIPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        GlitchInventoryAPI.init(new GlitchInventoryAPIImpl(GlitchInventoryAPIConfig.create(this)));
        getLogger().info("Successfully enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Successfully disabled!");
    }
}
