package me.memencio.viapluginsautoupdater.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

public final class HttpURLConnectionUtil {

    public static HttpURLConnection openHttpURLConnection(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/132.0.0.0 Safari/537.3");
        return connection;
    }
}
