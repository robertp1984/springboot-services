package org.softwarecave.springbootimages.images.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateImageParamsDTO {
    private String description;
    private Long width;
    private Long height;
}
