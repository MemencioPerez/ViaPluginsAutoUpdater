package me.memencio.viapluginsautoupdater.storage.loader;

import me.memencio.viapluginsautoupdater.storage.data.serializers.VersionSerializer;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class PluginConfigurationLoader {

    private final YamlConfigurationLoader loader;

    public PluginConfigurationLoader(Path path, String fileName) {
        Path destinationPath = path.resolve(fileName);
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create data directory", e);
            }
        }

        if (!Files.exists(destinationPath)) {
            try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
                Files.copy(Objects.requireNonNull(in, "Resource not found: " + fileName), destinationPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create config file", e);
            }
        }

        this.loader = YamlConfigurationLoader.builder()
                .defaultOptions(opts -> opts.serializers(build -> build.register(VersionSerializer.TYPE, VersionSerializer.INSTANCE)))
                .path(destinationPath)
                .build();
    }

    public CommentedConfigurationNode load() throws ConfigurateException {
        return loader.load();
    }

    public void save(ConfigurationNode node) throws ConfigurateException {
        loader.save(node);
    }
}
