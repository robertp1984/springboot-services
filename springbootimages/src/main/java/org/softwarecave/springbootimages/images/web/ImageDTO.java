package org.softwarecave.springbootimages.images.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO {
    @EqualsAndHashCode.Exclude
    private String id;
    private String originalFilename;
    private String contentType;
    private byte[] bytes;
    private long size;
    private String sha512;
    private Instant createdTime;
}
