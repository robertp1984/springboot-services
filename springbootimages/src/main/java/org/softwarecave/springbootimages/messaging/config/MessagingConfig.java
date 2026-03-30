package org.softwarecave.springbootimages.messaging.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MessagingConfig {

    public static final String IMAGES_SAVED_QUEUE_NAME = "queue.images.saved";
    public static final String IMAGES_DELETED_QUEUE_NAME = "queue.images.deleted";
    public static final String EXCHANGE_NAME = "operations";

    @Bean
    public Queue imagesSavedQueue() {
        return new Queue(IMAGES_SAVED_QUEUE_NAME);
    }

    @Bean
    public Queue imagesDeletedQueue() {
        return new Queue(IMAGES_DELETED_QUEUE_NAME);
    }

    @Bean
    public TopicExchange operationsExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding operationsImagesSavedBinding(@Qualifier("imagesSavedQueue") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("images.saved");
    }

    @Bean
    public Binding operationsImagesDeletedBinding(@Qualifier("imagesDeletedQueue") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("images.deleted");
    }

}
