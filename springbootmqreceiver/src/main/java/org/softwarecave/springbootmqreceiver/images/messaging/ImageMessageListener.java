package org.softwarecave.springbootmqreceiver.images.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.softwarecave.springbootmqreceiver.images.model.ActionType;
import org.softwarecave.springbootmqreceiver.images.model.ImageMessage;
import org.softwarecave.springbootmqreceiver.images.service.ImageMessageProcessor;
import org.softwarecave.springbootmqreceiver.images.config.MessagingConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@Slf4j
public class ImageMessageListener {

    private final ImageMessageProcessor imageMessageProcessor;

    public ImageMessageListener(ImageMessageProcessor imageMessageProcessor) {
        this.imageMessageProcessor = imageMessageProcessor;
    }

    @RabbitListener(queues = MessagingConfig.IMAGES_SAVED_QUEUE_NAME)
    public void receiveSavedMessage(@NonNull String messageString) {
        ImageMessage imageMessage = readImageMessage(messageString, ActionType.SAVE);
        imageMessageProcessor.process(imageMessage);
    }

    @RabbitListener(queues = MessagingConfig.IMAGES_DELETED_QUEUE_NAME)
    public void receiveDeletedMessage(@NonNull String messageString) {
        ImageMessage imageMessage = readImageMessage(messageString, ActionType.DELETE);
        imageMessageProcessor.process(imageMessage);
    }

    private ImageMessage readImageMessage(String messageString, ActionType actionType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            ImageMessage imageMessage = objectMapper.readValue(messageString, ImageMessage.class);
            imageMessage.setActionType(actionType);
            return imageMessage;
        } catch (JsonProcessingException e) {
            log.error("Failed to read or process the received message: %s".formatted(e.getMessage()), e);
            throw new IllegalArgumentException("Failed to read or process the received message", e);
        }
    }
}
