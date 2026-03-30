package org.softwarecave.springbootimages.images.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.NonNull;
import org.softwarecave.springbootimages.bedrock.ImageGenerationService;
import org.softwarecave.springbootimages.images.model.Image;
import org.softwarecave.springbootimages.images.model.NoSuchImageException;
import org.softwarecave.springbootimages.messaging.ImageMessageFactory;
import org.softwarecave.springbootimages.messaging.QueueSender;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@Scope("singleton")
public class ImageService {
    private final ImageRepository imageRepository;
    private final QueueSender queueSender;
    private final ImageGenerationService imageGenerationService;

    public ImageService(ImageRepository imageRepository, QueueSender queueSender,
                        ImageGenerationService imageGenerationService) {
        this.imageRepository = imageRepository;
        this.queueSender = queueSender;
        this.imageGenerationService = imageGenerationService;
    }

    public void saveImage(@NonNull Image image) throws JsonProcessingException {
        imageRepository.save(image);
        queueSender.publishImagesSavedMessage(ImageMessageFactory.createImageMessage(image));
    }

    public Optional<Image> getImage(@NonNull String id) {
        return imageRepository.findById(id);
    }

    public void deleteImage(@NonNull String id) throws JsonProcessingException {
        Optional<Image> image = imageRepository.findById(id);
        if (image.isPresent()) {
            imageRepository.deleteById(id);
            queueSender.publishImagesDeletedMessage(ImageMessageFactory.createImageMessage(image.get()));
        } else {
            throw new NoSuchImageException("Image with id: " + id + " does not exist");
        }
    }

    public Image generateAndSaveImage(@NonNull GenerateImageParams params) throws JsonProcessingException {
        Image image = imageGenerationService.generateImage(params);
        saveImage(image);
        return image;
    }
}
