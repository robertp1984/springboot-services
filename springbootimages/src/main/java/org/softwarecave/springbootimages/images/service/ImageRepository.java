package org.softwarecave.springbootimages.images.service;

import org.softwarecave.springbootimages.images.model.Image;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageRepository extends MongoRepository<Image, String> {
}
