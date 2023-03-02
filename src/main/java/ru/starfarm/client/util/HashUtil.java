package ru.starfarm.client.util;

import lombok.experimental.UtilityClass;
import lombok.val;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@UtilityClass
public class HashUtil {

    public MessageDigest messageDigest;

    static {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supports");
        }
    }

    public String hash(String string) {
        return hash0(string.getBytes(StandardCharsets.UTF_8));
    }

    private String hash0(byte[] bytes) {
        return hex(messageDigest.digest(bytes));
    }

    private String hex(byte[] bytes) {
        val builder = new StringBuilder();

        for (byte b : bytes) builder.append(String.format("%x", b));

        return builder.toString();
    }

}
