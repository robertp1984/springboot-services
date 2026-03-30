package org.softwarecave.springbootimages.messaging;

import lombok.NonNull;
import org.softwarecave.springbootimages.images.model.Image;

public class ImageMessageFactory {
    public static ImageMessage createImageMessage(@NonNull Image image) {
        return new ImageMessage(image.getId(), image.getOriginalFilename(), image.getContentType(), image.getCreatedTime());
    }
}
