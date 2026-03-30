package org.softwarecave.springbootimages.images.web.converter;

import org.softwarecave.springbootimages.images.model.Image;
import org.softwarecave.springbootimages.images.web.ImageDTO;

public class ImageDTOConverter {

    public static ImageDTO convertToDTO(Image image){
        if (image == null) {
            return null;
        }
        return new ImageDTO(
                image.getId(),
                image.getOriginalFilename(),
                image.getContentType(),
                image.getBytes(),
                image.getSize(),
                image.getSha512(),
                image.getCreatedTime()
        );
    }

    public static Image convertToImage(ImageDTO imageDTO){
        if (imageDTO == null) {
            return null;
        }
        return new Image(
                imageDTO.getId(),
                imageDTO.getOriginalFilename(),
                imageDTO.getContentType(),
                imageDTO.getBytes(),
                imageDTO.getSize(),
                imageDTO.getSha512(),
                imageDTO.getCreatedTime()
        );
    }
}
