package org.softwarecave.springbootimages.images.model;

import org.softwarecave.springbootimages.utils.SHA512Calculator;

import java.time.ZonedDateTime;
import java.util.UUID;

public class ImageBuilder {
    private final Image image = new Image();

    public ImageBuilder withId(String id) {
        image.setId(id);
        return this;
    }

    public ImageBuilder withOriginalFilename(String originalFilename) {
        image.setOriginalFilename(originalFilename);
        return this;
    }


    public ImageBuilder withContentType(String contentType) {
        image.setContentType(contentType);
        return this;
    }

    public ImageBuilder withBytes(byte[] bytes) {
        image.setBytes(bytes);
        image.setSize(bytes.length);
        image.setSha512(new SHA512Calculator().getHash(bytes));
        return this;
    }

    public Image build() {
        return image;
    }

    public ImageBuilder withUUID() {
        return withId(UUID.randomUUID().toString());
    }

    public ImageBuilder withCurrentDateTime() {
        image.setCreatedTime(ZonedDateTime.now().toInstant());
        return this;
    }
}
