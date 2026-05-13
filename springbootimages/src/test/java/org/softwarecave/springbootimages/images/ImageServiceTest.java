package org.softwarecave.springbootimages.images;

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
    void saveImage_savesAndPublishesMessage() {
        // given
        when(imageRepository.save(sampleImage)).thenReturn(sampleImage);

        // when
        imageService.saveImage(sampleImage);

        // then
        verify(imageRepository).save(sampleImage);
        verify(queueSender).publishImagesSavedMessage(any());
    }

    @Test
    void getImage_existingId_returnsOptional() {
        // given
        when(imageRepository.findById("abc")).thenReturn(Optional.of(sampleImage));

        // when
        Image result = imageService.getImage("abc");

        // then
        assertThat(result).isNotNull().isEqualTo(sampleImage);
        verify(imageRepository).findById("abc");
    }

    @Test
    void deleteImage_existingId_deletes() {
        // given
        when(imageRepository.findById("id1")).thenReturn(Optional.of(sampleImage));
        doNothing().when(imageRepository).delete(sampleImage);

        // when
        imageService.deleteImage("id1");

        // then
        verify(imageRepository).findById("id1");
        verify(imageRepository).delete(sampleImage);
    }

    @Test
    void deleteImage_nonExisting_throwsNoSuchImageException() {
        // given
        when(imageRepository.findById("nope")).thenReturn(Optional.empty());

        // when && then
        assertThrows(NoSuchImageException.class, () -> imageService.deleteImage("nope"));

        verify(imageRepository).findById("nope");
        verify(imageRepository, never()).deleteById(any());
    }

    @Test
    void generateAndSaveImageByDescription_generatesSavesAndReturnsImage() {
        // given
        GenerateImageParams request = new GenerateImageParams("desc", 800L, 600L);
        when(imageGenerationService.generateImage(request)).thenReturn(sampleImage);
        when(imageRepository.save(sampleImage)).thenAnswer(a -> a.getArgument(0));

        //when
        Image result = imageService.generateAndSaveImage(request);

        // then
        assertThat(result).isSameAs(sampleImage);
        verify(imageGenerationService).generateImage(request);
        verify(imageRepository).save(sampleImage);
        verify(queueSender).publishImagesSavedMessage(any());
    }
}
