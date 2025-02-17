package me.memencio.viapluginsautoupdater.storage.data.serializers;

import com.github.zafarkhaja.semver.ParseException;
import com.github.zafarkhaja.semver.Version;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.CoercionFailedException;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

public final class VersionSerializer implements TypeSerializer<Version> {

    public static final VersionSerializer INSTANCE = new VersionSerializer();
    public static final Class<Version> TYPE = Version.class;

    private VersionSerializer() {
    }

    @Override
    public Version deserialize(Type type, ConfigurationNode node) throws SerializationException {
        @Nullable Object value = node.rawScalar();
        if (value == null) {
            throw new SerializationException("Version cannot be null");
        }
        try {
            return Version.parse(value.toString());
        } catch (ParseException e) {
            throw new CoercionFailedException(value, "Version");
        }
    }

    @Override
    public void serialize(Type type, @Nullable Version obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            throw new SerializationException("Version is null");
        }
        node.set(obj.toString());
    }
}