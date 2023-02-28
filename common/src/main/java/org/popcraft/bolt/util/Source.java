package org.popcraft.bolt.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;

public final class Source {
    private final String type;
    private final String identifier;

    private Source(final String type, final String identifier) {
        this.type = type;
        this.identifier = identifier;
    }

    private Source(final String type) {
        this.type = type;
        this.identifier = type;
    }

    public static Source of(final String type, final String identifier) {
        return new Source(type, identifier);
    }

    public static Source of(final String type) {
        return new Source(type);
    }

    public static Source player(final UUID uuid) {
        return new Source(SourceType.PLAYER, uuid.toString());
    }

    public static Source password(final String password) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(password.getBytes(StandardCharsets.UTF_8));
            final StringBuilder hash = new StringBuilder();
            for (final byte b : messageDigest.digest()) {
                hash.append("%02x".formatted(b));
            }
            return new Source(SourceType.PASSWORD, hash.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Source parse(final String source) {
        if (source == null) {
            return null;
        }
        final int split = source.indexOf(':');
        if (split < 0) {
            return new Source(source);
        }
        return new Source(source.substring(0, split), source.substring(split + 1));
    }

    public String getType() {
        return type;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Source source = (Source) o;
        return Objects.equals(type, source.type) && Objects.equals(identifier, source.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, identifier);
    }

    @Override
    public String toString() {
        return type + ":" + identifier;
    }
}
