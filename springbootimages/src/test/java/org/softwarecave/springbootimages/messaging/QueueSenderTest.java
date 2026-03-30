package org.softwarecave.springbootimages.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QueueSenderTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private QueueSender queueSender;

    @Test
    void publishImagesSavedMessage_withValidMessage_sendsJsonMessageToExchange() throws Exception {
        ImageMessage msg = new ImageMessage("id-1", "file.png", "image/png", Instant.now());
        when(objectMapper.writeValueAsString(msg)).thenReturn("{\"id\":\"id-1\"}");

        queueSender.publishImagesSavedMessage(msg);

        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(rabbitTemplate, times(1)).send(anyString(), anyString(), captor.capture());
        Message sent = captor.getValue();
        String body = new String(sent.getBody(), StandardCharsets.UTF_8);
        assertEquals("{\"id\":\"id-1\"}", body);
    }

    @Test
    void publishImagesSavedMessage_objectMapperThrows_propagatesJsonProcessingExceptionAndDoesNotSend() throws Exception {
        ImageMessage msg = new ImageMessage("id-2", "file2.png", "image/png", Instant.now());
        when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("fail") {
        });

        assertThrows(JsonProcessingException.class, () -> queueSender.publishImagesSavedMessage(msg));
        verify(rabbitTemplate, never()).send(anyString(), anyString(), any(Message.class));
    }

    @Test
    void publishImagesSavedMessage_nullMessage_throwsNullPointerExceptionAndDoesNotSend() {
        assertThrows(NullPointerException.class, () -> queueSender.publishImagesSavedMessage(null));
        verify(rabbitTemplate, never()).send(anyString(), anyString(), any(Message.class));
    }

    @Test
    void publishImagesDeletedMessage_withValidMessage_sendsJsonMessageToExchange() throws Exception {
        ImageMessage msg = new ImageMessage("id-1", "file.png", "image/png", Instant.now());
        when(objectMapper.writeValueAsString(msg)).thenReturn("{\"id\":\"id-1\"}");

        queueSender.publishImagesDeletedMessage(msg);

        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(rabbitTemplate, times(1)).send(anyString(), anyString(), captor.capture());
        Message sent = captor.getValue();
        String body = new String(sent.getBody(), StandardCharsets.UTF_8);
        assertEquals("{\"id\":\"id-1\"}", body);
    }

    @Test
    void publishImagesDeletedMessage_objectMapperThrows_propagatesJsonProcessingExceptionAndDoesNotSend() throws Exception {
        ImageMessage msg = new ImageMessage("id-2", "file2.png", "image/png", Instant.now());
        when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("fail") {
        });

        assertThrows(JsonProcessingException.class, () -> queueSender.publishImagesDeletedMessage(msg));
        verify(rabbitTemplate, never()).send(anyString(), anyString(), any(Message.class));
    }

    @Test
    void publishImagesDeletedMessage_nullMessage_throwsNullPointerExceptionAndDoesNotSend() {
        assertThrows(NullPointerException.class, () -> queueSender.publishImagesDeletedMessage(null));
        verify(rabbitTemplate, never()).send(anyString(), anyString(), any(Message.class));
    }
}

