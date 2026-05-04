package org.softwarecave.springbootimages.images.web.converter;

import org.softwarecave.springbootimages.images.service.GenerateImageParams;
import org.softwarecave.springbootimages.images.web.GenerateImageParamsDTO;
import org.springframework.stereotype.Component;

@Component
public class GenerateImageParamsConverter {
    public GenerateImageParams toRequest(GenerateImageParamsDTO requestDTO) {
        return new GenerateImageParams(requestDTO.getDescription(), requestDTO.getWidth(), requestDTO.getHeight());
    }
}
