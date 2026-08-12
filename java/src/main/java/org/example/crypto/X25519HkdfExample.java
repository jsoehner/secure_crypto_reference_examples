package org.example.crypto;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.util.Arrays;
import javax.crypto.KeyAgreement;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class X25519HkdfExample {
    private X25519HkdfExample() {}

    public static KeyPair generateKeyPair() throws Exception {
        return KeyPairGenerator.getInstance("X25519").generateKeyPair();
    }

    public static byte[] sharedSecret(KeyPair local, PublicKey peerPublic) throws Exception {
        KeyAgreement agreement = KeyAgreement.getInstance("X25519");
        agreement.init(local.getPrivate());
        agreement.doPhase(peerPublic, true);
        return agreement.generateSecret();
    }

    // RFC 5869 HKDF-SHA-256 using standard JCA Mac, suitable for Java 17+.
    public static byte[] hkdfSha256(byte[] ikm, byte[] salt, byte[] info, int length) throws Exception {
        if (ikm == null || ikm.length != 32) {
            throw new IllegalArgumentException("IKM must be 32 bytes for X25519.");
        }
        if (length <= 0 || length > 255 * 32) throw new IllegalArgumentException("Invalid HKDF length");
        byte[] effectiveSalt = salt == null ? new byte[32] : salt.clone();
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(effectiveSalt, "HmacSHA256"));
        byte[] prk = mac.doFinal(ikm);
        byte[] okm = new byte[length];
        byte[] previous = new byte[0];
        int offset = 0;
        for (int counter = 1; offset < length; counter++) {
            mac.init(new SecretKeySpec(prk, "HmacSHA256"));
            mac.update(previous);
            if (info != null) mac.update(info);
            mac.update((byte) counter);
            previous = mac.doFinal();
            int copy = Math.min(previous.length, length - offset);
            System.arraycopy(previous, 0, okm, offset, copy);
            offset += copy;
        }
        Arrays.fill(prk, (byte) 0);
        Arrays.fill(previous, (byte) 0);
        return okm;
    }

    public static void main(String[] args) throws Exception {
        KeyPair alice = generateKeyPair();
        KeyPair bob = generateKeyPair();
        byte[] aliceSecret = sharedSecret(alice, bob.getPublic());
        byte[] bobSecret = sharedSecret(bob, alice.getPublic());
        CryptoSupport.require(CryptoSupport.constantTimeEquals(aliceSecret, bobSecret), "X25519 mismatch");
        byte[] salt = CryptoSupport.randomBytes(16); // Must be shared with the peer, not regenerated independently.
        byte[] info = "app-v1 aes-gcm key".getBytes(StandardCharsets.UTF_8);
        byte[] aliceKey = hkdfSha256(aliceSecret, salt, info, 32);
        byte[] bobKey = hkdfSha256(bobSecret, salt, info, 32);
        CryptoSupport.require(CryptoSupport.constantTimeEquals(aliceKey, bobKey), "HKDF mismatch");
        Arrays.fill(aliceSecret, (byte) 0);
        Arrays.fill(bobSecret, (byte) 0);
        System.out.println("X25519HkdfExample derivedKey=" + CryptoSupport.hex(aliceKey));
    }
}
