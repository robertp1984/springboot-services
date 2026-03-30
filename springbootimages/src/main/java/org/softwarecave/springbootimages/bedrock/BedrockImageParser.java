package org.softwarecave.springbootimages.bedrock;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.softwarecave.springbootimages.images.model.Image;
import org.softwarecave.springbootimages.images.model.ImageBuilder;
import org.springframework.http.MediaType;

import java.io.IOException;

@Slf4j
public class BedrockImageParser {

    final static String IMAGE_MEDIA_TYPE = MediaType.IMAGE_PNG_VALUE;
    private final static int MAX_FILENAME_LENGTH = 128;

    private final ObjectMapper objectMapper;

    public BedrockImageParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Image parseResponse(String description, byte[] responseBodyBytes) throws IOException {
        BedrockImageBodyResponse responseObject = objectMapper.readValue(responseBodyBytes, BedrockImageBodyResponse.class);

        if (responseObject != null && responseObject.error() == null && responseObject.hasImage()) {
            return new ImageBuilder()
                    .withOriginalFilename(createShortFilename(description))
                    .withBytes(responseObject.getFirstImageBytes())
                    .withUUID()
                    .withContentType(IMAGE_MEDIA_TYPE)
                    .withCurrentDateTime()
                    .build();
        } else {
            if (responseObject == null) {
                log.error("Response from image generator is null");
            } else if (responseObject.error() != null) {
                log.error("Response from image generator contains error: {}", responseObject.error());
            } else {
                log.error("Response from image generator contains no image");
            }
            throw new ImageGenerationException("Failed to generate image. No image present", null);
        }
    }

    private String createShortFilename(String description) {
        String extension = getShortFilenameExtension();
        String baseName = description
                .replace(" ", "_")
                .substring(0, Math.min(MAX_FILENAME_LENGTH - extension.length() - 1, description.length()));
        return baseName + "." + extension;
    }

    private String getShortFilenameExtension() {
        if (IMAGE_MEDIA_TYPE.equals(MediaType.IMAGE_PNG_VALUE)) {
            return "png";
        } else {
            throw new IllegalArgumentException("Unsupported image media type " + IMAGE_MEDIA_TYPE);
        }
    }

}
