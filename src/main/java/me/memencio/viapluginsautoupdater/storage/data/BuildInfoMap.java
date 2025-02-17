package me.memencio.viapluginsautoupdater.storage.data;

import me.memencio.viapluginsautoupdater.enums.ViaPlugin;
import me.memencio.viapluginsautoupdater.storage.data.objects.BuildInfo;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Map;

@ConfigSerializable
public final class BuildInfoMap {

    @SuppressWarnings("unused")
    private Map<ViaPlugin, BuildInfo> plugins;

    public void put(ViaPlugin plugin, BuildInfo buildData) {
        plugins.put(plugin, buildData);
    }

    public BuildInfo get(ViaPlugin plugin) {
        return plugins.get(plugin);
    }

    public void remove(ViaPlugin plugin) {
        plugins.remove(plugin);
    }
}
