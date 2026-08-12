package org.example.crypto;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HexFormat;

final class CryptoSupport {
    static final SecureRandom RNG = new SecureRandom();
    private static final HexFormat HEX = HexFormat.of();

    private CryptoSupport() {}

    static byte[] randomBytes(int length) {
        byte[] value = new byte[length];
        RNG.nextBytes(value);
        return value;
    }

    static String hex(byte[] value) {
        return HEX.formatHex(value);
    }

    static void require(boolean condition, String message) {
        if (!condition) throw new IllegalStateException(message);
    }

    static boolean constantTimeEquals(byte[] left, byte[] right) {
        return MessageDigest.isEqual(left, right);
    }
}
