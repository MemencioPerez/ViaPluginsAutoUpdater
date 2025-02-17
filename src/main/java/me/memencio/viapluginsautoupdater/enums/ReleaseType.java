package me.memencio.viapluginsautoupdater.enums;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.memencio.viapluginsautoupdater.updatechecker.objects.Release;
import me.memencio.viapluginsautoupdater.updatechecker.objects.impl.github.GithubRelease;
import me.memencio.viapluginsautoupdater.updatechecker.objects.impl.jenkins.JenkinsBuild;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public enum ReleaseType {
    RELEASE("https://api.github.com/repos/ViaVersion/{pluginName}/releases/latest", Collections.emptyList(), new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create(), GithubRelease.class),
    SNAPSHOT("https://ci.viaversion.com/job/{pluginName}/lastSuccessfulBuild/api/json", Collections.singletonList(
            url -> url.replace("-", "%20")
    ), new GsonBuilder().create(), JenkinsBuild.class),
    JAVA8("https://ci.viaversion.com/job/{pluginName}-Java8/lastSuccessfulBuild/api/json", Collections.emptyList(), SNAPSHOT.gson, SNAPSHOT.releaseClass);

    private final String buildInfoUrl;
    private final List<Function<String, String>> transformations;
    private final Gson gson;
    private final Class<? extends Release> releaseClass;

    ReleaseType(String url, List<Function<String, String>> transformations, Gson gson, Class<? extends Release> releaseClass) {
        this.buildInfoUrl = url;
        this.transformations = transformations;
        this.gson = gson;
        this.releaseClass = releaseClass;
    }

    public String getUrl(ViaPlugin plugin) {
        String transformedUrl = buildInfoUrl.replaceAll("\\{pluginName}", plugin.getName());
        for (Function<String, String> transformation : transformations) {
            transformedUrl = transformation.apply(transformedUrl);
        }
        return transformedUrl;
    }

    public Gson getGson() {
        return gson;
    }

    public Class<? extends Release> getReleaseClass() {
        return releaseClass;
    }
}
