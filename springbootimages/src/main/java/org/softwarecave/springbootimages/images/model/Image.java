package org.softwarecave.springbootimages.images.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "images")
public class Image {
    @Id
    @EqualsAndHashCode.Exclude
    private String id;
    private String originalFilename;
    private String contentType;
    private byte[] bytes;
    private long size;
    private String sha512;
    private Instant createdTime;
}
