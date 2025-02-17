package me.memencio.viapluginsautoupdater.updatechecker.objects.impl.jenkins;

import com.github.zafarkhaja.semver.Version;
import me.memencio.viapluginsautoupdater.enums.ViaPlugin;
import me.memencio.viapluginsautoupdater.updatechecker.objects.Release;

import java.util.Arrays;
import java.util.Optional;

public final class JenkinsBuild implements Release {

    private final JenkinsArtifact[] artifacts;
    private final int number;
    private final String url;

    public JenkinsBuild(JenkinsArtifact[] artifacts, int id, String url) {
        this.artifacts = artifacts;
        this.number = id;
        this.url = url;
    }

    @Override
    public Version getVersion() {
        return Version.parse(this.getVersionFromFileName());
    }

    @Override
    public int getBuildNumber() {
        return number;
    }

    @Override
    public String getDownloadUrl() {
        return this.getUrl() + "artifact/" + findValidArtifact().map(JenkinsArtifact::getRelativePath).orElse(null);
    }

    private String getName() {
        return findValidArtifact().map(JenkinsArtifact::getFileName).orElse(null);
    }

    private Optional<JenkinsArtifact> findValidArtifact() {
        return Arrays.stream(artifacts)
                .filter(artifact -> artifact.getFileName().endsWith(".jar") && !artifact.getFileName().contains("sources"))
                .findFirst();
    }

    private String getVersionFromFileName() {
        String fileName = this.getName();
        for (int i = ViaPlugin.values().length - 1; i != 0; i--) {
            fileName = fileName.replace(ViaPlugin.values()[i].getName(), "");
        }
        return fileName.substring(fileName.indexOf("-") + 1, fileName.lastIndexOf("."));
    }

    private String getUrl() {
        return url;
    }
}
