package org.softwarecave.springbootmqreceiver.images.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MessagingConfig {

    public static final String IMAGES_SAVED_QUEUE_NAME = "queue.images.saved";
    public static final String IMAGES_DELETED_QUEUE_NAME = "queue.images.deleted";

    @Bean
    public Queue imagesSavedQueue() {
        return new Queue(IMAGES_SAVED_QUEUE_NAME);
    }

    @Bean
    public Queue imagesDeletedQueue() {
        return new Queue(IMAGES_DELETED_QUEUE_NAME);
    }

}
