package org.softwarecave.springbootimages.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Objects;

public class SHA512Calculator {

    private static final String SHA_512 = "SHA-512";

    public String getHash(byte[] bytes) {
        Objects.requireNonNull(bytes, "Bytes must not be null");
        try {
            MessageDigest md = MessageDigest.getInstance(SHA_512);
            byte[] digest = md.digest(bytes);
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 algorithm not available", e);
        }
    }

}
