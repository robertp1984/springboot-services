package org.softwarecave.springbootmqreceiver.images.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwarecave.springbootmqreceiver.images.model.ActionType;
import org.softwarecave.springbootmqreceiver.images.model.ImageMessage;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ImageMessageProcessorTest {

    @Mock
    private ImageMessageService imageMessageService;

    @InjectMocks
    private ImageMessageProcessor imageMessageProcessor;

    @Test
    void processValidImageMessage_callsSaveOnceWithSameMessage() {
        ImageMessage message = new ImageMessage("id-1", "picture.png", "image/png", Instant.now(), ActionType.SAVE);

        imageMessageProcessor.process(message);

        verify(imageMessageService).save(message);
    }

    @Test
    void processNullImageMessage_throwsNullPointerExceptionAndDoesNotCallSave() {
        assertThrows(NullPointerException.class, () -> imageMessageProcessor.process(null));
        verifyNoInteractions(imageMessageService);
    }

    @Test
    void processImageMessageWithNullFields_stillCallsSave() {
        ImageMessage message = new ImageMessage();
        // fields intentionally left null

        imageMessageProcessor.process(message);

        verify(imageMessageService).save(message);
    }
}

