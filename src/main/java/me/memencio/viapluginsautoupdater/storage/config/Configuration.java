package me.memencio.viapluginsautoupdater.storage.config;

import me.memencio.viapluginsautoupdater.enums.ViaPlugin;
import me.memencio.viapluginsautoupdater.storage.config.objects.UpdateCheckerConfig;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Map;

@ConfigSerializable
@SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
public final class Configuration {

    private int checkInterval;
    private Map<ViaPlugin, UpdateCheckerConfig> plugins;

    public int getCheckInterval() {
        return checkInterval;
    }

    public UpdateCheckerConfig getUpdateCheckerConfig(ViaPlugin plugin) {
        return plugins.get(plugin);
    }
}
