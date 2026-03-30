package org.softwarecave.springbootimages.bedrock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.softwarecave.springbootimages.images.model.Image;
import org.softwarecave.springbootimages.images.service.GenerateImageParams;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class ImageGenerationService {

    private final String IMAGE_GEN_MODEL = "amazon.nova-canvas-v1:0";

    private final ObjectMapper objectMapper;

    public ImageGenerationService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Image generateImage(@NonNull GenerateImageParams params) {
        try (BedrockRuntimeClient client = createClient()) {

            String jsonRequest = createRequest(params);

            var response = client.invokeModel(request -> request.body(SdkBytes.fromUtf8String(jsonRequest))
                    .modelId(IMAGE_GEN_MODEL)
                    .accept(BedrockImageParser.IMAGE_MEDIA_TYPE));

            return new BedrockImageParser(objectMapper).parseResponse(params.getDescription(), response.body().asByteArray());
        } catch (Exception e) {
            log.error("Failed to generate image {} ", e.getMessage(), e);
            throw new ImageGenerationException("Could not generate image with description=%s".formatted(params.getDescription()), e);
        }
    }


    private String createRequest(GenerateImageParams request) throws JsonProcessingException {
        var seed = new BigInteger(31, new SecureRandom());

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("taskType", "TEXT_IMAGE");
        rootNode.putObject("textToImageParams")
                .put("text", request.getDescription());
        rootNode.putObject("imageGenerationConfig")
                .put("width", Optional.ofNullable(request.getWidth()).orElse(1024L))
                .put("height", Optional.ofNullable(request.getHeight()).orElse(768L))
                .put("quality", "standard")
                .put("cfgScale", 6.5)
                .put("seed", seed);

        return objectMapper.writeValueAsString(rootNode);
    }

    private BedrockRuntimeClient createClient() {
        return BedrockRuntimeClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.builder().build())
                .region(Region.US_EAST_1)
                .build();
    }

}
