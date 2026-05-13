package org.softwarecave.springbootimages.images.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    private String originalFilename;

    @NotBlank
    private String contentType;

    @NotNull
    @Size(min = 1)
    private byte[] bytes;

    @Positive
    private long size;

    @NotBlank
    private String sha512;

    @NotNull
    @PastOrPresent
    private Instant createdTime;
}
