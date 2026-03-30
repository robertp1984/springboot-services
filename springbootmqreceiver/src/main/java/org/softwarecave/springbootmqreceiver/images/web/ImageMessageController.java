package org.softwarecave.springbootmqreceiver.images.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.softwarecave.springbootmqreceiver.images.model.ImageMessage;
import org.softwarecave.springbootmqreceiver.images.service.ImageMessageService;
import org.softwarecave.springbootmqreceiver.images.web.converter.ImageMessageDTOConverter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/imageMessages")
@Slf4j
@Tag(name = "ImageMessageController", description = "This is a REST service to access image message events")
public class ImageMessageController {

    private final ImageMessageService imageMessageService;

    public ImageMessageController(ImageMessageService imageMessageService) {
        this.imageMessageService = imageMessageService;
    }

    @GetMapping
    @Operation(summary = "Returns a list of all image messages ")
    public List<ImageMessageDTO> getImageMessages() {
        log.info("Called getImageMessages");
        var imageMessages = imageMessageService.getAll();
        return imageMessages.stream().map(ImageMessageDTOConverter::convertToDTO).toList();
    }

    @GetMapping(value = "/{id}")
    public ImageMessageDTO getImageMessage(@PathVariable("id") String id) {
        log.info("Called getImageMessage");
        ImageMessage imageMessage = imageMessageService.findById(id);
        if (imageMessage != null) {
            return ImageMessageDTOConverter.convertToDTO(imageMessage);
        } else {
            throw new NoSuchElementException("No ImageMessage with ID=" + id);
        }
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImageMessages(@PathVariable("id") String id) {
        log.info("Called deleteImageMessages");
        imageMessageService.removeById(id);
    }
}
