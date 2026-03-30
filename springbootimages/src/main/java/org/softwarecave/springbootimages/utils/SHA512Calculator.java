package org.softwarecave.springbootimages.utils;

import lombok.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class SHA512Calculator {

    private static final String SHA_512 = "SHA-512";

    public String getHash(@NonNull byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance(SHA_512);
            byte[] digest = md.digest(bytes);
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 algorithm not available", e);
        }
    }

}
