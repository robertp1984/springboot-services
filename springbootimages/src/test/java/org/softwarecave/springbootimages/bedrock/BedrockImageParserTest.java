package org.softwarecave.springbootimages.bedrock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.softwarecave.springbootimages.images.model.Image;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BedrockImageParserTest {

    private BedrockImageParser parser;

    @BeforeEach
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        parser = new BedrockImageParser(objectMapper);
    }

    @Test
    void parseResponse_returnsImageAndPopulatesFields_whenResponseContainsBase64Image() throws Exception {

        String json = """
                {"images":["SGVsbG8="]}
                """;
        Image image = parser.parseResponse("Hello picture", json.getBytes(StandardCharsets.UTF_8));

        assertNotNull(image);
        assertEquals("image/png", image.getContentType());
        assertEquals("Hello_picture.png", image.getOriginalFilename());
    }

    @Test
    void parseResponse_throwsImageGenerationException_whenResponseJsonIsLiteralNull() {

        byte[] nullJson = "null".getBytes(StandardCharsets.UTF_8);
        assertThrows(org.softwarecave.springbootimages.bedrock.ImageGenerationException.class, () -> parser.parseResponse("desc", nullJson));
    }

    @Test
    void parseResponse_throwsImageGenerationException_whenImagesArrayIsEmpty() {

        String json = """
                {"images":[]}"
                """;
        assertThrows(org.softwarecave.springbootimages.bedrock.ImageGenerationException.class, () ->
                parser.parseResponse("desc", json.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void parseResponse_truncatesOriginalFilename_whenDescriptionExceedsMaxLength() throws Exception {

        String json = """
                {"images":["SGVsbG8="]}"
                """;
        String longDescription = "a".repeat(200);
        Image image = parser.parseResponse(longDescription, json.getBytes(StandardCharsets.UTF_8));

        assertNotNull(image.getOriginalFilename());
        assertTrue(image.getOriginalFilename().endsWith(".png"));
        assertEquals(128, image.getOriginalFilename().length());
    }
}
