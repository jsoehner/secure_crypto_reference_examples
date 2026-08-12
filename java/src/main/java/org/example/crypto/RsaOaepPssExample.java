package org.example.crypto;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

public final class RsaOaepPssExample {
    private RsaOaepPssExample() {}

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(3072, CryptoSupport.RNG);
        return generator.generateKeyPair();
    }

    private static OAEPParameterSpec oaepParameters() {
        return new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);
    }

    public static byte[] encrypt(PublicKey key, byte[] plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, oaepParameters(), CryptoSupport.RNG);
        return cipher.doFinal(plaintext);
    }

    public static byte[] decrypt(PrivateKey key, byte[] ciphertext) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, oaepParameters());
        return cipher.doFinal(ciphertext);
    }

    private static PSSParameterSpec pssParameters() {
        return new PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 32, 1);
    }

    public static byte[] sign(PrivateKey key, byte[] message) throws Exception {
        Signature signature = Signature.getInstance("RSASSA-PSS");
        signature.setParameter(pssParameters());
        signature.initSign(key, CryptoSupport.RNG);
        signature.update(message);
        return signature.sign();
    }

    public static boolean verify(PublicKey key, byte[] message, byte[] signatureValue) throws Exception {
        Signature signature = Signature.getInstance("RSASSA-PSS");
        signature.setParameter(pssParameters());
        signature.initVerify(key);
        signature.update(message);
        return signature.verify(signatureValue);
    }

    public static void main(String[] args) throws Exception {
        KeyPair keys = generateKeyPair();
        byte[] message = "small secret or content-encryption key".getBytes(StandardCharsets.UTF_8);
        byte[] ciphertext = encrypt(keys.getPublic(), message);
        CryptoSupport.require(Arrays.equals(message, decrypt(keys.getPrivate(), ciphertext)), "RSA-OAEP failed");
        byte[] signature = sign(keys.getPrivate(), message);
        CryptoSupport.require(verify(keys.getPublic(), message, signature), "RSA-PSS failed");
        System.out.println("RsaOaepPssExample encryption and signature OK");
    }
}
