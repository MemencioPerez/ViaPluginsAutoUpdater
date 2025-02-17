package me.memencio.viapluginsautoupdater.updatechecker.objects;

import com.github.zafarkhaja.semver.Version;
import me.memencio.viapluginsautoupdater.utils.HttpURLConnectionUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

public interface Release {

    Version getVersion();

    int getBuildNumber();

    String getDownloadUrl();

    default void downloadFile(Path destinationPath) {
        HttpURLConnection connection = null;
        try {
            connection = HttpURLConnectionUtil.openHttpURLConnection(getDownloadUrl());
            try (InputStream in = connection.getInputStream()) {
                Files.copy(in, destinationPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file from URL: " + getDownloadUrl(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
