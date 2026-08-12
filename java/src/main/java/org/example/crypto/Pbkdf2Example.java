package org.example.crypto;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class Pbkdf2Example {
    public record DerivedKey(byte[] salt, int iterations, byte[] key) {
        public DerivedKey {
            salt = salt.clone();
            key = key.clone();
        }
        @Override public byte[] salt() { return salt.clone(); }
        @Override public byte[] key() { return key.clone(); }
    }

    private Pbkdf2Example() {}

    public static DerivedKey derive(char[] password) throws Exception {
        byte[] salt = CryptoSupport.randomBytes(16);
        int iterations = 600_000; // Calibrate for the deployment environment.
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, 256);
        try {
            byte[] key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec).getEncoded();
            return new DerivedKey(salt, iterations, key);
        } finally {
            spec.clearPassword();
        }
    }

    public static boolean verify(char[] password, DerivedKey stored) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password, stored.salt(), stored.iterations(), 256);
        try {
            byte[] candidate = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec).getEncoded();
            try { return CryptoSupport.constantTimeEquals(candidate, stored.key()); }
            finally { Arrays.fill(candidate, (byte) 0); }
        } finally {
            spec.clearPassword();
        }
    }

    public static void main(String[] args) throws Exception {
        char[] password = "correct horse battery staple".toCharArray();
        try {
            DerivedKey stored = derive(password);
            CryptoSupport.require(verify(password, stored), "PBKDF2 verification failed");
            System.out.println("Pbkdf2Example salt=" + CryptoSupport.hex(stored.salt()));
        } finally {
            Arrays.fill(password, '\0');
        }
    }
}
