package me.memencio.viapluginsautoupdater.updatechecker;

import me.memencio.viapluginsautoupdater.enums.ReleaseType;
import me.memencio.viapluginsautoupdater.enums.ViaPlugin;
import me.memencio.viapluginsautoupdater.storage.data.objects.BuildInfo;
import me.memencio.viapluginsautoupdater.updatechecker.objects.Release;
import me.memencio.viapluginsautoupdater.utils.HttpURLConnectionUtil;

import java.io.*;
import java.net.*;
import java.util.Optional;

public final class ViaPluginUpdateChecker {

    private final ViaPlugin plugin;
    private final ReleaseType releaseType;
    private final BuildInfo buildInfo;

    public ViaPluginUpdateChecker(ViaPlugin plugin, ReleaseType releaseType, BuildInfo buildInfo) {
        this.plugin = plugin;
        this.releaseType = releaseType;
        this.buildInfo = buildInfo;
    }

    public Optional<Release> checkForNewReleases() {
        if (!plugin.getReleaseTypes().contains(releaseType)) {
            throw new IllegalArgumentException("Plugin " + plugin.getName() + " does not support release type " + releaseType + ".");
        }

        HttpURLConnection connection = null;
        try {
            connection = HttpURLConnectionUtil.openHttpURLConnection(releaseType.getUrl(plugin));
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                Release release = releaseType.getGson().fromJson(reader, releaseType.getReleaseClass());
                if (buildInfo == null || release.getVersion().isHigherThan(buildInfo.getVersion()) || release.getVersion().isEquivalentTo(buildInfo.getVersion()) && release.getBuildNumber() > buildInfo.getBuildNumber()) {
                    return Optional.of(release);
                } else {
                    return Optional.empty();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
