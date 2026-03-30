package org.softwarecave.springbootimages.images.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateImageParams {
    private String description;
    private Long width;
    private Long height;
}
