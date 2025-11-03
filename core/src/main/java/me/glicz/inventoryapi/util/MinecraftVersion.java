package me.glicz.inventoryapi.util;

import com.google.common.base.Suppliers;
import io.papermc.paper.ServerBuildInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

public record MinecraftVersion(int major, int minor, int patch) {
    private static final Supplier<MinecraftVersion> CURRENT_VERSION = Suppliers.memoize(() ->
            parseVersion(ServerBuildInfo.buildInfo().minecraftVersionId())
    );

    public static MinecraftVersion parseVersion(String version) {
        String[] split = version.split("\\.", 3);

        int major = Integer.parseInt(split[0]);
        int minor = Integer.parseInt(split[1]);
        int patch = split.length > 2 ? Integer.parseInt(split[2]) : 0;

        return new MinecraftVersion(major, minor, patch);
    }

    public static MinecraftVersion currentVersion() {
        return CURRENT_VERSION.get();
    }

    public boolean isAtLeast(MinecraftVersion other) {
        return isAtLeast(other.major, other.minor, other.patch);
    }

    public boolean isAtLeast(int major, int minor, int patch) {
        return this.major >= major && this.minor >= minor && this.patch >= patch;
    }

    @Override
    public String toString() {
        return StringUtils.joinWith(".", major, minor, patch);
    }
}
