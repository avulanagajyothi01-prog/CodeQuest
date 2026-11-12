package com.codequest.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordUtil {
    private static final int ITERATIONS = 210_000;
    private static final int KEY_LENGTH = 256;
    private PasswordUtil() { }

    public static String hash(String password) {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        byte[] derived = derive(password, salt, ITERATIONS);
        return "pbkdf2_sha256$" + ITERATIONS + "$" + Base64.getEncoder().encodeToString(salt)
                + "$" + Base64.getEncoder().encodeToString(derived);
    }

    public static boolean verify(String password, String stored) {
        try {
            String[] parts = stored.split("\\$");
            if (parts.length != 4 || !"pbkdf2_sha256".equals(parts[0])) return false;
            int rounds = Integer.parseInt(parts[1]);
            if (rounds < 100_000 || rounds > 2_000_000) return false;
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expected = Base64.getDecoder().decode(parts[3]);
            return MessageDigest.isEqual(expected, derive(password, salt, rounds));
        } catch (RuntimeException ex) { return false; }
    }

    private static byte[] derive(String password, byte[] salt, int rounds) {
        char[] chars = password.toCharArray();
        PBEKeySpec spec = new PBEKeySpec(chars, salt, rounds, KEY_LENGTH);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (Exception e) { throw new IllegalStateException("Password hashing failed", e); }
        finally { spec.clearPassword(); java.util.Arrays.fill(chars, '\0'); }
    }
}
