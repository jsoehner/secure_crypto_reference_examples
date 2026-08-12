package org.example.crypto;

import java.nio.charset.StandardCharsets;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class HmacExample {
    private HmacExample() {}

    public static byte[] compute(byte[] key, byte[] message) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key, "HmacSHA256"));
        return mac.doFinal(message);
    }

    public static boolean verify(byte[] key, byte[] message, byte[] expectedTag) throws Exception {
        return CryptoSupport.constantTimeEquals(compute(key, message), expectedTag);
    }

    public static void main(String[] args) throws Exception {
        byte[] key = CryptoSupport.randomBytes(32);
        byte[] message = "authenticated message".getBytes(StandardCharsets.UTF_8);
        byte[] tag = compute(key, message);
        CryptoSupport.require(verify(key, message, tag), "HMAC verification failed");
        System.out.println("HmacExample tag=" + CryptoSupport.hex(tag));
    }
}
