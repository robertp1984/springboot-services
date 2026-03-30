package org.softwarecave.springbootimages.bedrock;

import java.util.Base64;

public record BedrockImageBodyResponse(String[] images, String error, String maskImage) {
    public byte[] getFirstImageBytes() {
        return Base64.getDecoder().decode(images[0]);
    }
    public boolean hasImage() {
        return images != null && images.length > 0;
    }
}


