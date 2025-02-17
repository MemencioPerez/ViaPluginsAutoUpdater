package me.memencio.viapluginsautoupdater.utils;

import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class PluginsDirectoryUtil {

    public static Set<File> findPluginFiles(String pluginName, File pluginsDirectory) {
        File[] plugins = pluginsDirectory.listFiles();
        Set<File> pluginFiles = new HashSet<>();
        if (plugins != null) {
            for (File file : plugins) {
                if (file.isDirectory() || !file.getName().endsWith(".jar")) {
                    continue;
                }

                try (JarFile jarFile = new JarFile(file)) {
                    JarEntry entry = jarFile.getJarEntry("plugin.yml");

                    if (entry == null) {
                        continue;
                    }

                    PluginDescriptionFile description = new PluginDescriptionFile(jarFile.getInputStream(entry));
                    if (description.getName().equals(pluginName)) {
                        pluginFiles.add(file);
                    }
                } catch (InvalidDescriptionException | IOException ignored) {
                }
            }
        }
        return pluginFiles;
    }
}
