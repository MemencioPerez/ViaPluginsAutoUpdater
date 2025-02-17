package me.memencio.viapluginsautoupdater.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ViaPlugin {
    VIAVERSION("ViaVersion", ReleaseType.RELEASE, ReleaseType.SNAPSHOT, ReleaseType.JAVA8),
    VIABACKWARDS("ViaBackwards", VIAVERSION.getReleaseTypes()),
    VIAREWIND("ViaRewind", VIAVERSION.getReleaseTypes()),
    VIAREWIND_LEGACY_SUPPORT("ViaRewind-Legacy-Support", ReleaseType.RELEASE, ReleaseType.SNAPSHOT);

    private final String name;
    private final Set<ReleaseType> releaseTypes;

    ViaPlugin(String name, ReleaseType... releaseTypes) {
        this.name = name;
        this.releaseTypes = Arrays.stream(releaseTypes).collect(Collectors.toSet());
    }

    ViaPlugin(String name, Set<ReleaseType> releaseTypes) {
        this.name = name;
        this.releaseTypes = releaseTypes;
    }

    public String getName() {
        return name;
    }

    public Set<ReleaseType> getReleaseTypes() {
        return releaseTypes;
    }

    @Override
    public String toString() {
        return name;
    }
}
