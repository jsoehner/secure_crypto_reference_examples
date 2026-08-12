package org.example.crypto;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public final class AesGcmExample {
    public record Envelope(byte[] nonce, byte[] cipherTextAndTag) {
        public Envelope { nonce = nonce.clone(); cipherTextAndTag = cipherTextAndTag.clone(); }
        @Override public byte[] nonce() { return nonce.clone(); }
        @Override public byte[] cipherTextAndTag() { return cipherTextAndTag.clone(); }
    }

    private AesGcmExample() {}

    public static SecretKey generateKey() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(256, CryptoSupport.RNG);
        return generator.generateKey();
    }

    public static Envelope encrypt(SecretKey key, byte[] plaintext, byte[] aad) throws Exception {
        byte[] nonce = CryptoSupport.randomBytes(12);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, nonce));
        if (aad != null) cipher.updateAAD(aad);
        return new Envelope(nonce, cipher.doFinal(plaintext));
    }

    public static byte[] decrypt(SecretKey key, Envelope envelope, byte[] aad) throws Exception {
        if (envelope.nonce().length != 12) {
            throw new IllegalArgumentException("Invalid nonce length. Expected 12 bytes.");
        }
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, envelope.nonce()));
        if (aad != null) cipher.updateAAD(aad);
        return cipher.doFinal(envelope.cipherTextAndTag());
    }

    public static void main(String[] args) throws Exception {
        SecretKey key = generateKey();
        byte[] aad = "app=v1;purpose=reference".getBytes(StandardCharsets.UTF_8);
        byte[] plaintext = "confidential data".getBytes(StandardCharsets.UTF_8);
        Envelope envelope = encrypt(key, plaintext, aad);
        CryptoSupport.require(Arrays.equals(plaintext, decrypt(key, envelope, aad)), "AES-GCM round trip failed");

        byte[] tampered = envelope.cipherTextAndTag();
        tampered[0] ^= 1;
        try {
            decrypt(key, new Envelope(envelope.nonce(), tampered), aad);
            throw new IllegalStateException("Tampering was not detected");
        } catch (AEADBadTagException expected) {
            System.out.println("AesGcmExample round-trip OK; tampering rejected");
        }
    }
}
