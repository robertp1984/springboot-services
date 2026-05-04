package org.softwarecave.springbootimages.images.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateImageParamsDTO {

    @NotBlank
    private String description;

    @Positive
    private Long width;

    @Positive
    private Long height;
}
