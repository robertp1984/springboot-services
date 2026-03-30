package org.softwarecave.springbootimages.images;

import org.junit.jupiter.api.Test;
import org.softwarecave.springbootimages.images.model.Image;
import org.softwarecave.springbootimages.images.model.ImageBuilder;
import org.softwarecave.springbootimages.utils.SHA512Calculator;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageBuilderTest {

    private static final String ORIGINAL_FILENAME = "/aa.txt";
    private static final String CONTENT_TYPE = "plain/text";
    private static final byte[] BYTES = "Sample text".getBytes();

    @Test
    public void testBuilderWithAllArguments() {
        ImageBuilder imageBuilder = new ImageBuilder();
        Instant startTime = Instant.now();
        Image image = imageBuilder
                .withUUID()
                .withOriginalFilename(ORIGINAL_FILENAME)
                .withContentType(CONTENT_TYPE)
                .withBytes(BYTES)
                .withCurrentDateTime()
                .build();

        assertThat(image)
                .hasFieldOrPropertyWithValue("originalFilename", ORIGINAL_FILENAME)
                .hasFieldOrPropertyWithValue("contentType", CONTENT_TYPE)
                .hasFieldOrPropertyWithValue("bytes", BYTES)
                .matches(e -> e.getCreatedTime().toEpochMilli() - startTime.toEpochMilli() < 20000)
                .hasFieldOrPropertyWithValue("size", (long) BYTES.length)
                .hasFieldOrPropertyWithValue("sha512", new SHA512Calculator().getHash(BYTES));
    }
}
