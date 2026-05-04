package org.softwarecave.springbootimages.messaging;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.util.Objects;

import static org.softwarecave.springbootimages.messaging.config.MessagingConfig.EXCHANGE_NAME;

@Service
@RequiredArgsConstructor
public class QueueSender {

    private static final String IMAGES_SAVED_ROUTING_KEY = "images.saved";
    private static final String IMAGES_DELETED_ROUTING_KEY = "images.deleted";

    private final RabbitTemplate rabbitTemplate;
    private final JsonMapper jsonMapper;

    public void publishImagesSavedMessage(ImageMessage message) {
        publishImagesMessage(message, IMAGES_SAVED_ROUTING_KEY);
    }

    public void publishImagesDeletedMessage(ImageMessage message) {
        publishImagesMessage(message, IMAGES_DELETED_ROUTING_KEY);
    }

    private void publishImagesMessage(ImageMessage message, String imagesSavedRoutingKey) {
        Objects.requireNonNull(message, "Message must not be null");

        String json = jsonMapper.writeValueAsString(message);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, imagesSavedRoutingKey, json);
    }

}
