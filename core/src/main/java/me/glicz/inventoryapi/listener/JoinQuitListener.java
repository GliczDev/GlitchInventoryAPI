package me.glicz.inventoryapi.listener;

import me.glicz.inventoryapi.GlitchInventory;
import me.glicz.inventoryapi.GlitchInventoryAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class JoinQuitListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        GlitchInventoryAPI.get().nmsBridge().injectPlayer(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Optional.ofNullable(GlitchInventory.get(e.getPlayer())).ifPresent(glitchInventory -> glitchInventory.close(e.getPlayer()));
        GlitchInventoryAPI.get().nmsBridge().uninjectPlayer(e.getPlayer());
    }
}
