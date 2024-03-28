package me.glicz.inventoryapi.nms;

import lombok.experimental.UtilityClass;
import me.glicz.inventoryapi.nms.exception.UnsupportedVersionException;
import me.glicz.inventoryapi.nms.v1_20_R3.NMS_v1_20_R3;
import org.bukkit.Bukkit;

@UtilityClass
public class NMSVersionHandler {
    public static NMS getNmsInstance(boolean useLatest) {
        if (useLatest) {
            return new NMS_v1_20_R3();
        } else {
            String version = Bukkit.getMinecraftVersion();
            return switch (version) {
                case "1.20.3", "1.20.4" -> new NMS_v1_20_R3();
                default -> throw new UnsupportedVersionException(version);
            };
        }
    }
}
