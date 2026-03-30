package org.softwarecave.springbootmqreceiver.images.web.converter;

import org.softwarecave.springbootmqreceiver.images.model.ImageMessage;
import org.softwarecave.springbootmqreceiver.images.web.ImageMessageDTO;

public class ImageMessageDTOConverter {
    public static ImageMessageDTO convertToDTO(ImageMessage imageMessage) {
        if (imageMessage == null) {
            return null;
        }
        return new ImageMessageDTO(
                imageMessage.getId(),
                imageMessage.getOriginalFilename(),
                imageMessage.getContentType(),
                imageMessage.getCreatedTime(),
                imageMessage.getActionType()
        );
    }

    public static ImageMessage convertToEntity(ImageMessageDTO imageMessageDTO) {
        if (imageMessageDTO == null) {
            return null;
        }
        return new ImageMessage(
                imageMessageDTO.getId(),
                imageMessageDTO.getOriginalFilename(),
                imageMessageDTO.getContentType(),
                imageMessageDTO.getCreatedTime(),
                imageMessageDTO.getActionType()
        );
    }
}
