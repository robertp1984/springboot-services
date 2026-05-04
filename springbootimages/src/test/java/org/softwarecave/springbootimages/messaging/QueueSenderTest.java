package org.softwarecave.springbootimages.messaging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import tools.jackson.databind.json.JsonMapper;

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
    private JsonMapper jsonMapper;

    @InjectMocks
    private QueueSender queueSender;

    @Test
    void publishImagesSavedMessage_withValidMessage_sendsJsonMessageToExchange() throws Exception {
        ImageMessage msg = new ImageMessage("id-1", "file.png", "image/png", Instant.now());
        when(jsonMapper.writeValueAsString(msg)).thenReturn("{\"id\":\"id-1\"}");

        queueSender.publishImagesSavedMessage(msg);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), captor.capture());
        String sent = captor.getValue();
        assertEquals("{\"id\":\"id-1\"}", sent);
    }

    @Test
    void publishImagesSavedMessage_jsonMapperThrows_propagatesRuntimeExceptionAndDoesNotSend() {
        ImageMessage msg = new ImageMessage("id-2", "file2.png", "image/png", Instant.now());
        when(jsonMapper.writeValueAsString(any())).thenThrow(new RuntimeException("fail"));

        assertThrows(RuntimeException.class, () -> queueSender.publishImagesSavedMessage(msg));
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
        when(jsonMapper.writeValueAsString(msg)).thenReturn("{\"id\":\"id-1\"}");

        queueSender.publishImagesDeletedMessage(msg);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), captor.capture());
        String sent = captor.getValue();
        assertEquals("{\"id\":\"id-1\"}", sent);
    }

    @Test
    void publishImagesDeletedMessage_jsonMapperThrows_propagatesRuntimeExceptionAndDoesNotSend() throws Exception {
        ImageMessage msg = new ImageMessage("id-2", "file2.png", "image/png", Instant.now());
        when(jsonMapper.writeValueAsString(any())).thenThrow(new RuntimeException("fail"));

        assertThrows(RuntimeException.class, () -> queueSender.publishImagesDeletedMessage(msg));
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), anyString());
    }

    @Test
    void publishImagesDeletedMessage_nullMessage_throwsNullPointerExceptionAndDoesNotSend() {
        assertThrows(NullPointerException.class, () -> queueSender.publishImagesDeletedMessage(null));
        verify(rabbitTemplate, never()).send(anyString(), anyString(), any(Message.class));
    }
}

