package org.softwarecave.springbootimages.images.web.converter;

import org.softwarecave.springbootimages.images.service.GenerateImageParams;
import org.softwarecave.springbootimages.images.web.GenerateImageParamsDTO;

public class GenerateImageParamsConverter {
    public static GenerateImageParams toRequest(GenerateImageParamsDTO requestDTO) {
        return new GenerateImageParams(requestDTO.getDescription(), requestDTO.getWidth(), requestDTO.getHeight());
    }
}
