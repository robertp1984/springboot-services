package org.softwarecave.springbootmqreceiver.images.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwarecave.springbootmqreceiver.images.model.ActionType;
import org.softwarecave.springbootmqreceiver.images.model.ImageMessage;
import org.softwarecave.springbootmqreceiver.images.service.ImageMessageProcessor;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ImageMessageListenerTest {

    @InjectMocks
    private ImageMessageListener imageMessageListener;

    @Mock
    private ImageMessageProcessor imageMessageProcessor;


    private static final String FILENAME1 = "filename1.txt";
    private static final String TEXT_PLAIN = "text/plain";
    private static final Instant INSTANT = Instant.now();


    @Test
    public void testReceiveSavedMessage_Null() {
        assertThrows(NullPointerException.class, () -> imageMessageListener.receiveSavedMessage(null));
    }

    @Test
    public void testReceiveSavedMessage_Empty() {
        assertThrows(IllegalArgumentException.class, () -> imageMessageListener.receiveSavedMessage(""));
    }

    @Test
    public void testReceiveSavedMessage_InvalidJson() {
        assertThrows(IllegalArgumentException.class, () -> imageMessageListener.receiveSavedMessage("{aa:22}"));
    }

    @Test
    public void testReceiveSavedMessage_ValidAndComplete() throws JsonProcessingException {
        // given
        ImageMessage sourceImageMessage = new ImageMessage(UUID.randomUUID().toString(),
                FILENAME1, TEXT_PLAIN, INSTANT, ActionType.SAVE);
        String sourceImageMessageJson = getObjectMapper().writeValueAsString(sourceImageMessage);
        doNothing().when(imageMessageProcessor).process(sourceImageMessage);

        // when
        imageMessageListener.receiveSavedMessage(sourceImageMessageJson);

        // then
        ArgumentCaptor<ImageMessage> captor = ArgumentCaptor.forClass(ImageMessage.class);
        verify(imageMessageProcessor, times(1)).process(captor.capture());
        ImageMessage capturedImageMessage = captor.getValue();
        assertThat(capturedImageMessage)
                .hasFieldOrPropertyWithValue("originalFilename", FILENAME1)
                .hasFieldOrPropertyWithValue("contentType", TEXT_PLAIN)
                .hasFieldOrPropertyWithValue("createdTime", INSTANT)
                .hasFieldOrPropertyWithValue("actionType", ActionType.SAVE);
    }

    @Test
    public void testReceiveSavedMessage_ValidAndNotComplete() throws JsonProcessingException {
        // given
        ImageMessage sourceImageMessage = new ImageMessage(UUID.randomUUID().toString(),
                FILENAME1, TEXT_PLAIN, null, ActionType.SAVE);
        String sourceImageMessageJson = getObjectMapper().writeValueAsString(sourceImageMessage);
        doNothing().when(imageMessageProcessor).process(sourceImageMessage);

        // when
        imageMessageListener.receiveSavedMessage(sourceImageMessageJson);

        // then
        ArgumentCaptor<ImageMessage> captor = ArgumentCaptor.forClass(ImageMessage.class);
        verify(imageMessageProcessor, times(1)).process(captor.capture());
        ImageMessage capturedImageMessage = captor.getValue();
        assertThat(capturedImageMessage)
                .hasFieldOrPropertyWithValue("originalFilename", FILENAME1)
                .hasFieldOrPropertyWithValue("contentType", TEXT_PLAIN)
                .hasFieldOrPropertyWithValue("createdTime", null)
                .hasFieldOrPropertyWithValue("actionType", ActionType.SAVE);
    }

    @Test
    public void testReceiveDeletedMessage_ValidAndComplete() throws JsonProcessingException {
        // given
        ImageMessage sourceImageMessage = new ImageMessage(UUID.randomUUID().toString(),
                FILENAME1, TEXT_PLAIN, INSTANT, ActionType.DELETE);
        String sourceImageMessageJson = getObjectMapper().writeValueAsString(sourceImageMessage);
        doNothing().when(imageMessageProcessor).process(sourceImageMessage);

        // when
        imageMessageListener.receiveDeletedMessage(sourceImageMessageJson);

        // then
        ArgumentCaptor<ImageMessage> captor = ArgumentCaptor.forClass(ImageMessage.class);
        verify(imageMessageProcessor, times(1)).process(captor.capture());
        ImageMessage capturedImageMessage = captor.getValue();
        assertThat(capturedImageMessage)
                .hasFieldOrPropertyWithValue("originalFilename", FILENAME1)
                .hasFieldOrPropertyWithValue("contentType", TEXT_PLAIN)
                .hasFieldOrPropertyWithValue("createdTime", INSTANT)
                .hasFieldOrPropertyWithValue("actionType", ActionType.DELETE);
    }

    @Test
    public void testReceiveDeletedMessage_ValidAndNotComplete() throws JsonProcessingException {
        // given
        ImageMessage sourceImageMessage = new ImageMessage(UUID.randomUUID().toString(),
                FILENAME1, TEXT_PLAIN, null, ActionType.DELETE);
        String sourceImageMessageJson = getObjectMapper().writeValueAsString(sourceImageMessage);
        doNothing().when(imageMessageProcessor).process(sourceImageMessage);

        // when
        imageMessageListener.receiveDeletedMessage(sourceImageMessageJson);

        // then
        ArgumentCaptor<ImageMessage> captor = ArgumentCaptor.forClass(ImageMessage.class);
        verify(imageMessageProcessor, times(1)).process(captor.capture());
        ImageMessage capturedImageMessage = captor.getValue();
        assertThat(capturedImageMessage)
                .hasFieldOrPropertyWithValue("originalFilename", FILENAME1)
                .hasFieldOrPropertyWithValue("contentType", TEXT_PLAIN)
                .hasFieldOrPropertyWithValue("createdTime", null)
                .hasFieldOrPropertyWithValue("actionType", ActionType.DELETE);
    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
