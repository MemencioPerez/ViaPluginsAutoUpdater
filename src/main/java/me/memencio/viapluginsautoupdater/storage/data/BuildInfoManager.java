package me.memencio.viapluginsautoupdater.storage.data;

import me.memencio.viapluginsautoupdater.enums.ViaPlugin;
import me.memencio.viapluginsautoupdater.storage.loader.PluginConfigurationLoader;
import me.memencio.viapluginsautoupdater.storage.data.objects.BuildInfo;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;

import java.nio.file.Path;

public final class BuildInfoManager {

    private final PluginConfigurationLoader loader;
    private final CommentedConfigurationNode node;
    private final BuildInfoMap map;

    public BuildInfoManager(Path path, String fileName) {
        try {
            this.loader = new PluginConfigurationLoader(path, fileName);
            this.node = loader.load();
            this.map = node.get(BuildInfoMap.class);
        } catch (ConfigurateException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public void put(ViaPlugin plugin, BuildInfo buildData) {
        map.put(plugin, buildData);
    }

    public BuildInfo get(ViaPlugin string) {
        return map.get(string);
    }

    public void remove(ViaPlugin plugin) {
        map.remove(plugin);
    }

    public void save() {
        try {
            loader.save(node.set(BuildInfoMap.class, map));
        } catch (ConfigurateException e) {
            throw new RuntimeException("Failed to save downloaded builds info", e);
        }
    }
}
