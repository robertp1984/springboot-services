package org.softwarecave.springbootimages.images;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.softwarecave.springbootimages.images.model.Image;
import org.softwarecave.springbootimages.images.model.ImageBuilder;
import org.softwarecave.springbootimages.images.model.NoSuchImageException;
import org.softwarecave.springbootimages.images.service.ImageService;
import org.softwarecave.springbootimages.images.web.ImagesController;
import org.softwarecave.springbootimages.utils.SHA512Calculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImagesController.class)
public class ImagesControllerTest {

    private static final String FILENAME1 = "sampleFile.txt";
    private static final byte[] BYTES = "CONTENT".getBytes();
    private static final String CONTENT_TYPE = "text/plain";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ImageService imageService;

    @Test
    public void testUploadImage_Valid() throws Exception {
        doNothing().when(imageService).saveImage(any(Image.class));

        mockMvc.perform(multipart("/api/v1/images/")
                        .file(new MockMultipartFile("image", FILENAME1, CONTENT_TYPE, BYTES)))
                .andExpect(status().isCreated());

        ArgumentCaptor<Image> imageArgumentCaptor = ArgumentCaptor.forClass(Image.class);
        verify(imageService).saveImage(imageArgumentCaptor.capture());
        Image imageToSave = imageArgumentCaptor.getValue();
        assertThat(imageToSave)
                .matches(e -> e.getId() != null)
                .hasFieldOrPropertyWithValue("originalFilename", FILENAME1)
                .hasFieldOrPropertyWithValue("contentType", CONTENT_TYPE)
                .hasFieldOrPropertyWithValue("bytes", BYTES)
                .hasFieldOrPropertyWithValue("size", (long) BYTES.length)
                .hasFieldOrPropertyWithValue("sha512", new SHA512Calculator().getHash(BYTES))
                .matches(e -> e.getCreatedTime() != null);
    }

    @Test
    public void testGetImage_ValidId() throws Exception {
        Image image = new ImageBuilder()
                .withId("5")
                .withOriginalFilename(FILENAME1)
                .withBytes(BYTES)
                .withCurrentDateTime()
                .build();
        when(imageService.getImage("5")).thenReturn(Optional.of(image));

        mockMvc.perform(get("/api/v1//images/5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value("5"))
                .andExpect(jsonPath("$.originalFilename").value(FILENAME1));

        verify(imageService).getImage("5");
    }

    @Test
    public void testGetImage_ImageDoesNotExists() throws Exception {
        when(imageService.getImage("5")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1//images/5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteImage_ValidId() throws Exception {
        doNothing().when(imageService).deleteImage("6");

        mockMvc.perform(delete("/api/v1//images/6"))
                .andExpect(status().isNoContent());

        verify(imageService).deleteImage("6");
    }

    @Test
    public void testDeleteImage_ImageDoesNotExists() throws Exception {
        doThrow(new NoSuchImageException("No Such Image")).when(imageService).deleteImage("6");

        mockMvc.perform(delete("/api/v1//images/6"))
                .andExpect(status().isNotFound());

        verify(imageService).deleteImage("6");
    }

}
