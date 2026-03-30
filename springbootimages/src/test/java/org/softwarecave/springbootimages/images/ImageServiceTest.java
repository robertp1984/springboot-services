package org.softwarecave.springbootimages.images;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwarecave.springbootimages.bedrock.ImageGenerationService;
import org.softwarecave.springbootimages.images.model.Image;
import org.softwarecave.springbootimages.images.model.ImageBuilder;
import org.softwarecave.springbootimages.images.model.NoSuchImageException;
import org.softwarecave.springbootimages.images.service.GenerateImageParams;
import org.softwarecave.springbootimages.images.service.ImageRepository;
import org.softwarecave.springbootimages.images.service.ImageService;
import org.softwarecave.springbootimages.messaging.QueueSender;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private QueueSender queueSender;

    @Mock
    private ImageGenerationService imageGenerationService;

    @InjectMocks
    private ImageService imageService;


    private Image sampleImage;

    @BeforeEach
    public void setUp() {
        sampleImage = new ImageBuilder()
                .withId("1")
                .withOriginalFilename("a.png")
                .withContentType("image/png")
                .withBytes("data".getBytes())
                .withCurrentDateTime()
                .build();
    }

    @Test
    void saveImage_savesAndPublishesMessage() throws JsonProcessingException {

        when(imageRepository.save(sampleImage)).thenReturn(sampleImage);

        imageService.saveImage(sampleImage);

        verify(imageRepository).save(sampleImage);
        verify(queueSender).publishImagesSavedMessage(any());
    }

    @Test
    void getImage_existingId_returnsOptional() {
        when(imageRepository.findById("abc")).thenReturn(Optional.of(sampleImage));

        Optional<Image> result = imageService.getImage("abc");

        verify(imageRepository).findById("abc");
        assertThat(result).isPresent().contains(sampleImage);
    }

    @Test
    void deleteImage_existingId_deletes() throws JsonProcessingException {
        when(imageRepository.findById("id1")).thenReturn(Optional.of(sampleImage));
        doNothing().when(imageRepository).deleteById("id1");

        imageService.deleteImage("id1");

        verify(imageRepository).findById("id1");
        verify(imageRepository).deleteById("id1");
    }

    @Test
    void deleteImage_nonExisting_throwsNoSuchImageException() {
        when(imageRepository.findById("nope")).thenReturn(Optional.empty());

        assertThrows(NoSuchImageException.class, () -> imageService.deleteImage("nope"));

        verify(imageRepository).findById("nope");
        verify(imageRepository, never()).deleteById(any());
    }

    @Test
    void generateAndSaveImageByDescription_generatesSavesAndReturnsImage() throws JsonProcessingException {
        GenerateImageParams request = new GenerateImageParams("desc", 800L, 600L);
        when(imageGenerationService.generateImage(request)).thenReturn(sampleImage);

        Image result = imageService.generateAndSaveImage(request);

        assertThat(result).isSameAs(sampleImage);
        verify(imageGenerationService).generateImage(request);
        verify(imageRepository).save(sampleImage);
        verify(queueSender).publishImagesSavedMessage(any());

    }
}

