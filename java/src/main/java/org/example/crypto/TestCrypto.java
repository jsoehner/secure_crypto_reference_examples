package org.example.crypto;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Arrays;

public final class TestCrypto {
    public static void main(String[] args) throws Exception {
        // Hashing
        byte[] digest = HashingExample.sha256("test data".getBytes(StandardCharsets.UTF_8));
        assert digest.length == 32;

        // HMAC
        byte[] key = CryptoSupport.randomBytes(32);
        byte[] message = "test message".getBytes(StandardCharsets.UTF_8);
        byte[] tag = HmacExample.compute(key, message);
        assert HmacExample.verify(key, message, tag);
        assert !HmacExample.verify(key, (message + "!").getBytes(StandardCharsets.UTF_8), tag);

        // PBKDF2
        char[] password = "test password".toCharArray();
        Pbkdf2Example.DerivedKey stored = Pbkdf2Example.derive(password);
        assert Pbkdf2Example.verify(password, stored);
        assert !Pbkdf2Example.verify("wrong password".toCharArray(), stored);

        // AES-GCM
        javax.crypto.SecretKey aesKey = AesGcmExample.generateKey();
        byte[] plaintext = "test message".getBytes(StandardCharsets.UTF_8);
        byte[] aad = "aad".getBytes(StandardCharsets.UTF_8);
        AesGcmExample.Envelope envelope = AesGcmExample.encrypt(aesKey, plaintext, aad);
        assert Arrays.equals(plaintext, AesGcmExample.decrypt(aesKey, envelope, aad));

        // RSA-OAEP-PSS
        KeyPair keys = RsaOaepPssExample.generateKeyPair();
        byte[] rsaMsg = "rsa message".getBytes(StandardCharsets.UTF_8);
        byte[] ciphertext = RsaOaepPssExample.encrypt(keys.getPublic(), rsaMsg);
        assert Arrays.equals(rsaMsg, RsaOaepPssExample.decrypt(keys.getPrivate(), ciphertext));
        byte[] signature = RsaOaepPssExample.sign(keys.getPrivate(), rsaMsg);
        assert RsaOaepPssExample.verify(keys.getPublic(), rsaMsg, signature);

        // Ed25519
        KeyPair edKeys = Ed25519Example.generateKeyPair();
        byte[] edMsg = "ed25519 message".getBytes(StandardCharsets.UTF_8);
        byte[] edSig = Ed25519Example.sign(edKeys.getPrivate(), edMsg);
        assert Ed25519Example.verify(edKeys.getPublic(), edMsg, edSig);

        // X25519-HKDF
        KeyPair alice = X25519HkdfExample.generateKeyPair();
        KeyPair bob = X25519HkdfExample.generateKeyPair();
        byte[] salt = CryptoSupport.randomBytes(16);
        byte[] info = "info".getBytes(StandardCharsets.UTF_8);
        byte[] aliceKey = X25519HkdfExample.hkdfSha256(X25519HkdfExample.sharedSecret(alice, bob.getPublic()), salt, info, 32);
        byte[] bobKey = X25519HkdfExample.hkdfSha256(X25519HkdfExample.sharedSecret(bob, alice.getPublic()), salt, info, 32);
        assert CryptoSupport.constantTimeEquals(aliceKey, bobKey);

        // AES-KeyWrap
        javax.crypto.SecretKey kek = AesKeyWrapExample.generateAesKey();
        javax.crypto.SecretKey contentKey = AesKeyWrapExample.generateAesKey();
        byte[] wrapped = AesKeyWrapExample.wrap(kek, contentKey);
        javax.crypto.SecretKey recovered = AesKeyWrapExample.unwrap(kek, wrapped);
        assert Arrays.equals(contentKey.getEncoded(), recovered.getEncoded());

        System.out.println("All Java tests passed!");
    }
}
