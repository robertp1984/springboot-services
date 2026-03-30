package org.softwarecave.springbootmqreceiver.images.service;

import org.softwarecave.springbootmqreceiver.images.model.ImageMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageMessageRepository extends MongoRepository<ImageMessage, String> {
}
