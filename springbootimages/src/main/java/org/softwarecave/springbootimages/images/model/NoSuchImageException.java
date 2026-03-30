package org.softwarecave.springbootimages.images.model;

public class NoSuchImageException extends RuntimeException {
    public NoSuchImageException(String s) {
        super(s);
    }
}
