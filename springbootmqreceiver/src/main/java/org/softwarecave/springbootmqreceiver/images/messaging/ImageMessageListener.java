package org.softwarecave.springbootmqreceiver.images.messaging;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.softwarecave.springbootmqreceiver.images.config.MessagingConfig;
import org.softwarecave.springbootmqreceiver.images.model.ActionType;
import org.softwarecave.springbootmqreceiver.images.model.ImageMessage;
import org.softwarecave.springbootmqreceiver.images.service.ImageMessageProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

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
            JsonMapper jsonMapper = new JsonMapper();
            ImageMessage imageMessage = jsonMapper.readValue(messageString, ImageMessage.class);
            imageMessage.setActionType(actionType);
            return imageMessage;
        } catch (Exception e) {
            log.error("Failed to read or process the received message: %s".formatted(e.getMessage()), e);
            throw new IllegalArgumentException("The image message must be valid JSON");
        }
    }
}
