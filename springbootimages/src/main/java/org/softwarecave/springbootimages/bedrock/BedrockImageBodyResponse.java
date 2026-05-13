package org.softwarecave.springbootimages.bedrock;

import java.util.Base64;

public record BedrockImageBodyResponse(String[] images, String error, String maskImage) {
    public byte[] getFirstImageBytes() {
        if (hasImage()) {
            return Base64.getDecoder().decode(images[0]);
        } else {
            throw new ImageGenerationException("No image was generated");
        }
    }

    public boolean hasImage() {
        return images != null && images.length > 0 && images[0] != null;
    }
}


