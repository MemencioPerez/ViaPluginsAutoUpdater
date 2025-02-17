package me.memencio.viapluginsautoupdater.storage.data.objects;

import com.github.zafarkhaja.semver.Version;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@SuppressWarnings("unused")
public final class BuildInfo {

    private Version version;
    private int buildNumber;

    public BuildInfo() {}

    public BuildInfo(Version version, int buildNumber) {
        this.version = version;
        this.buildNumber = buildNumber;
    }

    public Version getVersion() {
        return version;
    }

    public int getBuildNumber() {
        return buildNumber;
    }
}
