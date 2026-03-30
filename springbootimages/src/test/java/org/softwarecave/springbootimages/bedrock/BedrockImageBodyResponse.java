package org.softwarecave.springbootimages.bedrock;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class BedrockImageBodyResponseTest {

    @Test
    void hasImage_returnsFalse_whenImagesIsNull() {
        BedrockImageBodyResponse resp = new BedrockImageBodyResponse(null, null, null);
        assertFalse(resp.hasImage());
    }

    @Test
    void hasImage_returnsFalse_whenImagesEmpty() {
        BedrockImageBodyResponse resp = new BedrockImageBodyResponse(new String[0], null, null);
        assertFalse(resp.hasImage());
    }

    @Test
    void getFirstImageBytes_decodesBase64() {
        String base64 = "SGVsbG8="; // "Hello"
        BedrockImageBodyResponse resp = new BedrockImageBodyResponse(new String[]{base64}, null, null);
        byte[] expected = "Hello".getBytes(StandardCharsets.UTF_8);
        assertArrayEquals(expected, resp.getFirstImageBytes());
    }

}

