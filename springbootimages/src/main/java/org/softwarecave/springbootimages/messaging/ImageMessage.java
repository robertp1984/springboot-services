package org.softwarecave.springbootimages.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ImageMessage {
    private String id;
    @NonNull
    private String originalFilename;
    @NonNull
    private String contentType;
    @NonNull
    private Instant createdTime;
}
