package org.softwarecave.springbootimages.images.service;

import org.apache.commons.lang3.StringUtils;
import org.softwarecave.springbootimages.images.model.ImageValidationException;

public class GenerateImageParamsValidator {
    public static void validate(GenerateImageParams params) {
        if (params == null || StringUtils.isBlank(params.getDescription())) {
            throw new ImageValidationException("Description for the image must be specified");
        }
        if (params.getWidth() != null && params.getWidth() <= 0) {
            throw new ImageValidationException("Width for the image must be positive");
        }
        if (params.getHeight() != null && params.getHeight() <= 0) {
            throw new ImageValidationException("Height for the image must be positive");
        }
    }
}
