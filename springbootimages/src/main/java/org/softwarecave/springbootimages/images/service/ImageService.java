package org.softwarecave.springbootimages.images.service;

import lombok.RequiredArgsConstructor;
import org.softwarecave.springbootimages.bedrock.ImageGenerationService;
import org.softwarecave.springbootimages.images.model.Image;
import org.softwarecave.springbootimages.images.model.ImageValidationException;
import org.softwarecave.springbootimages.images.model.NoSuchImageException;
import org.softwarecave.springbootimages.messaging.ImageMessageFactory;
import org.softwarecave.springbootimages.messaging.QueueSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final QueueSender queueSender;
    private final ImageGenerationService imageGenerationService;

    public Image saveImage(Image image) {
        if (image == null) {
            throw new ImageValidationException("Image must not be null");
        }
        Image savedImage = imageRepository.save(image);
        queueSender.publishImagesSavedMessage(ImageMessageFactory.createImageMessage(savedImage));
        return savedImage;
    }

    @Transactional(readOnly = true)
    public Image getImage(String id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new NoSuchImageException("Image with id %s does not exist".formatted(id)));
    }

    public void deleteImage(String id) {
        if (id == null) {
            throw new ImageValidationException("Id of the image must not be null");
        }

        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new NoSuchImageException("Image with id %s does not exist".formatted(id)));
        imageRepository.delete(image);
        queueSender.publishImagesDeletedMessage(ImageMessageFactory.createImageMessage(image));
    }

    public Image generateAndSaveImage(GenerateImageParams params) {
        GenerateImageParamsValidator.validate(params);
        Image generatedImage = imageGenerationService.generateImage(params);
        return saveImage(generatedImage);
    }

}
