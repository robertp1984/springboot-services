package org.softwarecave.springbootimages.images.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.softwarecave.springbootimages.images.model.Image;
import org.softwarecave.springbootimages.images.model.ImageBuilder;
import org.softwarecave.springbootimages.images.service.GenerateImageParams;
import org.softwarecave.springbootimages.images.service.ImageService;
import org.softwarecave.springbootimages.images.model.NoSuchImageException;
import org.softwarecave.springbootimages.images.web.converter.GenerateImageParamsConverter;
import org.softwarecave.springbootimages.images.web.converter.ImageDTOConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@Transactional
@RequestMapping("/api/v1/images")
@Tag(name = "Images Controller", description = "Controller to upload, download and list images")
@Slf4j
public class ImagesController {

    private final ImageService imageService;

    public ImagesController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Uploads the image or any other file into the database")
    public void uploadImage(@RequestParam("image") @NonNull MultipartFile image) throws IOException {
        log.info("Uploading image {}", image.getOriginalFilename());

        Image imageObject = new ImageBuilder()
                .withUUID()
                .withOriginalFilename(image.getOriginalFilename())
                .withContentType(image.getContentType())
                .withBytes(image.getBytes())
                .withCurrentDateTime()
                .build();

        imageService.saveImage(imageObject);

        log.info("Image uploaded ID={}", imageObject.getId());
    }

    @PostMapping(value = "/generatedImage",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Generates a new image using AI and stores into database",
            description = "The operation can be accessed by sending request object with description 'sunny day' using post to this sample URI http://localhost:8081/images/generatedImage/ The generated image will be returned in the response.")
    public ResponseEntity<byte[]> newGeneratedImage(@RequestBody @NonNull GenerateImageParamsDTO paramsDTO) throws IOException {
        GenerateImageParams params = GenerateImageParamsConverter.toRequest(paramsDTO);
        log.info("Generating a new image with params {}", params);

        Image imageObject = imageService.generateAndSaveImage(params);

        log.info("Image generated and uploaded with id={}", imageObject.getId());
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(imageObject.getContentType()))
                .body(imageObject.getBytes());
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    @Operation(summary = "Fetches the image or any other file from the database based on its ID")
    @ResponseStatus(HttpStatus.OK)
    public ImageDTO getImage(@PathVariable("id") String id) {
        Optional<Image> image = imageService.getImage(id);
        if (image.isPresent()) {
            return ImageDTOConverter.convertToDTO(image.get());
        } else {
            throw new NoSuchImageException("No image with ID " + id);
        }
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deletes the image or any other file from the database based on the ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(@PathVariable("id") String id) throws JsonProcessingException {
        imageService.deleteImage(id);
    }

}
