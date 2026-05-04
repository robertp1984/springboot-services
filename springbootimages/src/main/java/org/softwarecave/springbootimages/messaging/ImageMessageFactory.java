package org.softwarecave.springbootimages.messaging;

import lombok.NonNull;
import org.softwarecave.springbootimages.images.model.Image;

import java.util.Objects;

public class ImageMessageFactory {
    public static ImageMessage createImageMessage(Image image) {
        Objects.requireNonNull(image, "Image must be not null");
        return new ImageMessage(image.getId(), image.getOriginalFilename(), image.getContentType(), image.getCreatedTime());
    }
}
