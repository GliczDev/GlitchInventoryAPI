package me.glicz.inventoryapi.item.builder;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.common.io.BaseEncoding;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class SkullBuilder extends AbstractItemBuilder<SkullBuilder, SkullMeta> {
    private static final BaseEncoding BASE64 = BaseEncoding.base64();

    SkullBuilder() {
        super(new ItemStack(Material.PLAYER_HEAD));
    }

    public OfflinePlayer owningPlayer() {
        return itemMeta.getOwningPlayer();
    }

    public SkullBuilder owningPlayer(OfflinePlayer owningPlayer) {
        itemMeta.setOwningPlayer(owningPlayer);
        return this;
    }

    public PlayerProfile playerProfile() {
        return itemMeta.getPlayerProfile();
    }

    public SkullBuilder playerProfile(@Nullable PlayerProfile playerProfile) {
        itemMeta.setPlayerProfile(playerProfile);
        return this;
    }

    public String value() {
        PlayerProfile profile = playerProfile();
        if (profile == null) {
            return null;
        }

        return profile.getProperties().stream()
                .filter(property -> property.getName().equals("textures"))
                .map(ProfileProperty::getValue)
                .findFirst()
                .orElse(null);
    }

    public SkullBuilder value(@NotNull String value) {
        PlayerProfile profile = itemMeta.getPlayerProfile();
        if (profile == null) {
            profile = Bukkit.createProfile(UUID.randomUUID());
        }

        profile.setProperty(new ProfileProperty("textures", value));

        return playerProfile(profile);
    }

    @SuppressWarnings("HttpUrlsUsage")
    public SkullBuilder url(@NotNull String url) {
        if (!url.startsWith("http://textures.minecraft.net/texture/")) {
            url = "http://textures.minecraft.net/texture/" + url;
        }

        return value(BASE64.encode("{textures:{SKIN:{url:\"%s\"}}}".formatted(url).getBytes()));
    }
}
