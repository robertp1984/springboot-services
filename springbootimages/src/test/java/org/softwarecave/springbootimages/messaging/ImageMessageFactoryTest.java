package org.softwarecave.springbootimages.messaging;

import org.junit.jupiter.api.Test;
import org.softwarecave.springbootimages.images.model.Image;
import org.softwarecave.springbootimages.images.model.ImageBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ImageMessageFactoryTest {

    @Test
    void createImageMessage_fromImage_mapsAllRelevantFields() {
        Image image = new ImageBuilder()
                .withId("id-123")
                .withOriginalFilename("pic.png")
                .withContentType("image/png")
                .withBytes(new byte[]{1, 2, 3})
                .withCurrentDateTime()
                .build();

        ImageMessage message = ImageMessageFactory.createImageMessage(image);

        assertThat(message)
                .hasFieldOrPropertyWithValue("id", "id-123")
                .hasFieldOrPropertyWithValue("originalFilename", "pic.png")
                .hasFieldOrPropertyWithValue("contentType", "image/png");
        assertThat(message.getCreatedTime()).isEqualTo(image.getCreatedTime());
    }

    @Test
    void createImageMessage_withNullImage_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ImageMessageFactory.createImageMessage(null));
    }
}

