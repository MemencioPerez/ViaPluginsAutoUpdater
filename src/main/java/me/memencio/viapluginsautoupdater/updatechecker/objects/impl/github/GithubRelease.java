package me.memencio.viapluginsautoupdater.updatechecker.objects.impl.github;

import com.github.zafarkhaja.semver.Version;
import me.memencio.viapluginsautoupdater.updatechecker.objects.Release;

public final class GithubRelease implements Release {

    private final String tagName;
    private final GithubAsset[] assets;

    public GithubRelease(String tagName, GithubAsset[] assets) {
        this.tagName = tagName;
        this.assets = assets;
    }

    @Override
    public Version getVersion() {
        return Version.parse(tagName.replace("v", ""));
    }

    @Override
    public int getBuildNumber() {
        return 0;
    }

    @Override
    public String getDownloadUrl() {
        return assets[0].getBrowserDownloadUrl();
    }
}
