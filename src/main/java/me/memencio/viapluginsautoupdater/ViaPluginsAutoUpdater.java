package me.memencio.viapluginsautoupdater;

import me.memencio.viapluginsautoupdater.storage.config.Configuration;
import me.memencio.viapluginsautoupdater.storage.config.objects.UpdateCheckerConfig;
import me.memencio.viapluginsautoupdater.enums.ReleaseType;
import me.memencio.viapluginsautoupdater.enums.ViaPlugin;
import me.memencio.viapluginsautoupdater.storage.loader.PluginConfigurationLoader;
import me.memencio.viapluginsautoupdater.storage.data.BuildInfoManager;
import me.memencio.viapluginsautoupdater.storage.data.objects.BuildInfo;
import me.memencio.viapluginsautoupdater.updatechecker.ViaPluginUpdateChecker;
import me.memencio.viapluginsautoupdater.updatechecker.objects.Release;
import me.memencio.viapluginsautoupdater.utils.PluginsDirectoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.spongepowered.configurate.ConfigurateException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

public final class ViaPluginsAutoUpdater extends JavaPlugin {

    private Logger logger;
    private Configuration config;
    private BuildInfoManager cache;
    private BukkitTask checkUpdatesTimerTask;

    @Override
    public void onLoad() {
        this.logger = getLogger();
        try {
            PluginConfigurationLoader loader = new PluginConfigurationLoader(getDataFolder().toPath(), "config.yml");
            this.config = loader.load().get(Configuration.class);
        } catch (ConfigurateException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
        this.cache = new BuildInfoManager(getDataFolder().toPath(), "downloaded-builds-info.yml");
    }

    @Override
    public void onEnable() {
        logger.info("ViaPluginsAutoUpdater has been enabled!");
        long checkInterval = config.getCheckInterval();
        if (checkInterval > 0L) {
            checkUpdatesTimerTask = getServer().getScheduler().runTaskTimerAsynchronously(this, this::checkUpdatesForAllPlugins, 0L, checkInterval * 60L * 20L);
            logger.info("Checking for updates every " + checkInterval + " minutes.");
        }
    }

    @Override
    public void onDisable() {
        logger = null;
        config = null;
        checkUpdatesTimerTask.cancel();
    }

    private void checkUpdatesForAllPlugins() {
        File pluginsDirectory = getDataFolder().getParentFile();
        for (ViaPlugin plugin : ViaPlugin.values()) {
            String name = plugin.getName();
            UpdateCheckerConfig updateCheckerConfig = config.getUpdateCheckerConfig(plugin);
            boolean updateCheckEnabled = updateCheckerConfig.isEnabled();
            if (!updateCheckEnabled) {
                continue;
            }

            Set<File> viaPluginFiles = PluginsDirectoryUtil.findPluginFiles(name, pluginsDirectory);
            Predicate<File> invalidPluginFile = file -> !file.getName().equals(name + ".jar");
            if (viaPluginFiles.stream().anyMatch(invalidPluginFile)) {
                logger.warning("A " + name + " invalid plugin file was found. Make sure to only have " + name + ".jar in the plugins directory. Skipping update...");
                continue;
            }

            boolean pluginFilesAlreadyExist = !viaPluginFiles.isEmpty();
            if (!pluginFilesAlreadyExist) {
                cache.remove(plugin);
            }

            logger.info("Checking for updates for " + name + "...");
            ReleaseType releaseType = updateCheckerConfig.getReleaseType();
            ViaPluginUpdateChecker updateChecker = new ViaPluginUpdateChecker(plugin, releaseType, cache.get(plugin));
            Optional<Release> newRelease = updateChecker.checkForNewReleases();
            if (newRelease.isPresent()) {
                Path destinationPath = pluginsDirectory.toPath();
                if (pluginFilesAlreadyExist) {
                    destinationPath = Bukkit.getUpdateFolderFile().toPath();
                }

                if (!Files.exists(destinationPath)) {
                    try {
                        Files.createDirectory(destinationPath);
                    } catch (IOException e) {
                        logger.warning("Failed to create the update folder to download the new version of " + name + ". Skipping update...");
                        continue;
                    }
                }

                Release release = newRelease.get();
                logger.info("Found update for " + name + " (version: " + release.getVersion() + (release.getBuildNumber() > 0 ? ", build number: " + release.getBuildNumber() : "") + ").");
                logger.info("Downloading new version...");
                release.downloadFile(destinationPath.resolve(name + ".jar"));
                cache.put(plugin, new BuildInfo(release.getVersion(), release.getBuildNumber()));
                logger.info("Downloaded new version of " + name + " successfully.");
            } else {
                logger.info("No updates found for " + name + ".");
            }

            cache.save();
        }
    }
}
