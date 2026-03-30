package org.softwarecave.springbootimages.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.softwarecave.springbootimages.messaging.config.MessagingConfig.EXCHANGE_NAME;

@Service
@Transactional
public class QueueSender {

    private static final String IMAGES_SAVED_ROUTING_KEY = "images.saved";
    private static final String IMAGES_DELETED_ROUTING_KEY = "images.deleted";

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public QueueSender(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishImagesSavedMessage(@NonNull ImageMessage message) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(message);

        Message jsonMsg = new SimpleMessageConverter().toMessage(json, null);
        rabbitTemplate.send(EXCHANGE_NAME, IMAGES_SAVED_ROUTING_KEY, jsonMsg);
    }

    public void publishImagesDeletedMessage(@NonNull ImageMessage message) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(message);

        Message jsonMsg = new SimpleMessageConverter().toMessage(json, null);
        rabbitTemplate.send(EXCHANGE_NAME, IMAGES_DELETED_ROUTING_KEY, jsonMsg);
    }
}
