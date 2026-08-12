package org.example.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public final class HashingExample {
    private HashingExample() {}

    public static byte[] sha256(byte[] data) throws Exception {
        return MessageDigest.getInstance("SHA-256").digest(data);
    }

    public static void main(String[] args) throws Exception {
        byte[] digest = sha256("secure coding reference".getBytes(StandardCharsets.UTF_8));
        System.out.println("HashingExample SHA-256=" + CryptoSupport.hex(digest));
    }
}
