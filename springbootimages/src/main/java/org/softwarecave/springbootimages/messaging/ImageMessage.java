package org.softwarecave.springbootimages.messaging;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ImageMessage {
    private String id;

    @NotBlank
    private String originalFilename;

    @NotBlank
    private String contentType;

    @NotNull
    @PastOrPresent
    private Instant createdTime;
}
