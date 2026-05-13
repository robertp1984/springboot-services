package org.softwarecave.springbootimages.bedrock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.softwarecave.springbootimages.images.model.Image;
import org.softwarecave.springbootimages.images.model.ImageBuilder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class BedrockImageParser {

    static final String IMAGE_MEDIA_TYPE = MediaType.IMAGE_PNG_VALUE;
    private final static int MAX_FILENAME_LENGTH = 128;

    private final JsonMapper jsonMapper;

    public Image parseResponse(String description, byte[] responseBodyBytes) {
        if (responseBodyBytes == null) {
            throw new ImageGenerationException("Failed to generate image due to null response");
        }

        BedrockImageBodyResponse responseObject;
        try {
            responseObject = jsonMapper.readValue(responseBodyBytes, BedrockImageBodyResponse.class);
        } catch (JacksonException e) {
            throw new ImageGenerationException("Failed to generate image due to issue with parsing response body", e);
        }

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
            throw new ImageGenerationException("Failed to generate image. No image present");
        }
    }

    private String createShortFilename(String description) {
        String extension = getShortFilenameExtension();
        String baseName = description
                .replaceAll("[^a-zA-Z0-9._-]", "_")
                .substring(0, Math.min(MAX_FILENAME_LENGTH - extension.length() - 1, description.length()));
        return baseName + "." + extension;
    }

    private String getShortFilenameExtension() {
        return "png";
    }

}
