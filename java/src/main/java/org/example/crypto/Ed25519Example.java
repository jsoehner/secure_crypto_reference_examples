package org.example.crypto;

import java.nio.charset.StandardCharsets;
import java.security.*;

public final class Ed25519Example {
    private Ed25519Example() {}

    public static KeyPair generateKeyPair() throws Exception {
        return KeyPairGenerator.getInstance("Ed25519").generateKeyPair();
    }

    public static byte[] sign(PrivateKey key, byte[] message) throws Exception {
        Signature signature = Signature.getInstance("Ed25519");
        signature.initSign(key);
        signature.update(message);
        return signature.sign();
    }

    public static boolean verify(PublicKey key, byte[] message, byte[] signatureValue) throws Exception {
        Signature signature = Signature.getInstance("Ed25519");
        signature.initVerify(key);
        signature.update(message);
        return signature.verify(signatureValue);
    }

    public static void main(String[] args) throws Exception {
        KeyPair keys = generateKeyPair();
        byte[] message = "signed message".getBytes(StandardCharsets.UTF_8);
        byte[] signature = sign(keys.getPrivate(), message);
        CryptoSupport.require(verify(keys.getPublic(), message, signature), "Ed25519 failed");
        System.out.println("Ed25519Example signature=" + CryptoSupport.hex(signature));
    }
}
