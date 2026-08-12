package org.example.crypto;

import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public final class AesKeyWrapExample {
    private AesKeyWrapExample() {}

    public static SecretKey generateAesKey() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(256, CryptoSupport.RNG);
        return generator.generateKey();
    }

    public static byte[] wrap(SecretKey keyEncryptionKey, SecretKey keyToWrap) throws Exception {
        Cipher cipher = Cipher.getInstance("AESWrap");
        cipher.init(Cipher.WRAP_MODE, keyEncryptionKey);
        return cipher.wrap(keyToWrap);
    }

    public static SecretKey unwrap(SecretKey keyEncryptionKey, byte[] wrapped) throws Exception {
        Cipher cipher = Cipher.getInstance("AESWrap");
        cipher.init(Cipher.UNWRAP_MODE, keyEncryptionKey);
        return (SecretKey) cipher.unwrap(wrapped, "AES", Cipher.SECRET_KEY);
    }

    public static void main(String[] args) throws Exception {
        SecretKey kek = generateAesKey();
        SecretKey contentKey = generateAesKey();
        byte[] wrapped = wrap(kek, contentKey);
        SecretKey recovered = unwrap(kek, wrapped);
        CryptoSupport.require(Arrays.equals(contentKey.getEncoded(), recovered.getEncoded()), "AES key wrap failed");
        System.out.println("AesKeyWrapExample wrapped=" + CryptoSupport.hex(wrapped));
    }
}
