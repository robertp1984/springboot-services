package org.softwarecave.springbootmqreceiver.images.service;

import lombok.NonNull;
import org.softwarecave.springbootmqreceiver.images.model.ImageMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ImageMessageService {

    private final ImageMessageRepository imageMessageRepository;

    public ImageMessageService(ImageMessageRepository imageMessageRepository) {
        this.imageMessageRepository = imageMessageRepository;
    }

    public ImageMessage save(@NonNull ImageMessage imageMessage) {
        return imageMessageRepository.save(imageMessage);
    }

    public List<ImageMessage> getAll() {
        return imageMessageRepository.findAll();
    }

    public ImageMessage findById(@NonNull String id) {
        return imageMessageRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public void removeById(@NonNull String id) {
        imageMessageRepository.deleteById(id);
    }

}
