package org.softwarecave.springbootimages.bedrock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.softwarecave.springbootimages.images.model.Image;
import org.softwarecave.springbootimages.images.service.GenerateImageParams;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

import java.util.Optional;
import java.util.random.RandomGenerator;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageGenerationService {

    public static final long DEFAULT_WIDTH = 1024L;
    public static final long DEFAULT_HEIGHT = 768L;
    public static final double CONFIG_SCALE = 6.5;
    private final String IMAGE_GEN_MODEL = "amazon.nova-canvas-v1:0";

    private final JsonMapper jsonMapper;
    private final BedrockImageParser bedrockImageParser;

    public Image generateImage(GenerateImageParams params) {
        try (BedrockRuntimeClient client = createClient()) {

            String jsonRequest = createRequest(params);

            var response = client.invokeModel(request -> request.body(SdkBytes.fromUtf8String(jsonRequest))
                    .modelId(IMAGE_GEN_MODEL)
                    .accept(BedrockImageParser.IMAGE_MEDIA_TYPE));

            return bedrockImageParser.parseResponse(params.getDescription(), response.body().asByteArray());
        } catch (Exception e) {
            log.error("Failed to generate image with description {}", params.getDescription(), e);
            throw new ImageGenerationException("Could not generate image with description=%s".formatted(params.getDescription()), e);
        }
    }

    private String createRequest(GenerateImageParams request) {
        var seed = RandomGenerator.getDefault().nextInt(100);

        ObjectNode rootNode = jsonMapper.createObjectNode();
        rootNode.put("taskType", "TEXT_IMAGE");
        rootNode.putObject("textToImageParams")
                .put("text", request.getDescription());
        rootNode.putObject("imageGenerationConfig")
                .put("width", Optional.ofNullable(request.getWidth()).orElse(DEFAULT_WIDTH))
                .put("height", Optional.ofNullable(request.getHeight()).orElse(DEFAULT_HEIGHT))
                .put("quality", "standard")
                .put("cfgScale", CONFIG_SCALE)
                .put("seed", seed);

        return jsonMapper.writeValueAsString(rootNode);
    }

    private BedrockRuntimeClient createClient() {
        return BedrockRuntimeClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.builder().build())
                .region(Region.US_EAST_1)
                .build();
    }

}
