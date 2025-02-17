package me.memencio.viapluginsautoupdater.storage.config.objects;

import me.memencio.viapluginsautoupdater.enums.ReleaseType;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal", "CanBeFinal"})
public final class UpdateCheckerConfig {

    private boolean enabled = true;
    private ReleaseType releaseType = ReleaseType.RELEASE;

    public boolean isEnabled() {
        return enabled;
    }

    public ReleaseType getReleaseType() {
        return releaseType;
    }
}
