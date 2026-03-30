package org.softwarecave.springbootmqreceiver.images.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.softwarecave.springbootmqreceiver.images.model.ActionType;
import org.softwarecave.springbootmqreceiver.images.model.ImageMessage;
import org.softwarecave.springbootmqreceiver.images.service.ImageMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ImageMessageController.class)
public class ImageMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ImageMessageService imageMessageService;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void getImageMessages_returnsListAsJson() throws Exception {
        ImageMessage msg1 = new ImageMessage("id-1", "a.png", "image/png", Instant.now(), ActionType.SAVE);
        ImageMessage msg2 = new ImageMessage("id-2", "b.jpg", "image/jpeg", Instant.now(), ActionType.DELETE);
        when(imageMessageService.getAll()).thenReturn(List.of(msg1, msg2));

        mockMvc.perform(get("/api/v1/imageMessages").accept(MediaType.APPLICATION_JSON)
                        .header("Origin", "http://localhost:4200"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].id").value("id-1"))
                .andExpect(jsonPath("$[1].id").value("id-2"));

        verify(imageMessageService).getAll();
    }

    @Test
    public void getImageMessage_existingId_returnsImageMessageJson() throws Exception {
        ImageMessage msg = new ImageMessage("found-id", "file.txt", "text/plain", Instant.now(), ActionType.SAVE);
        when(imageMessageService.findById("found-id")).thenReturn(msg);

        mockMvc.perform(get("/api/v1/imageMessages/found-id").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value("found-id"))
                .andExpect(jsonPath("$.originalFilename").value("file.txt"))
                .andExpect(jsonPath("$.contentType").value("text/plain"))
                .andExpect(jsonPath("$.actionType").value("SAVE"));

        verify(imageMessageService).findById("found-id");
    }

    @Test
    public void getImageMessage_missingId_returnsNotFound() throws Exception {
        when(imageMessageService.findById("missing")).thenReturn(null);

        mockMvc.perform(get("/api/v1/imageMessages/missing").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(imageMessageService).findById("missing");
    }

    @Test
    public void deleteImageMessages_existingId_returnsNoContent() throws Exception {
        doNothing().when(imageMessageService).removeById("to-delete");

        mockMvc.perform(delete("/api/v1/imageMessages/to-delete"))
                .andExpect(status().isNoContent());

        verify(imageMessageService).removeById("to-delete");
    }

    @Test
    public void deleteImageMessages_missingId_returnsNotFound() throws Exception {
        doThrow(new NoSuchElementException("no such")).when(imageMessageService).removeById("bad-id");

        mockMvc.perform(delete("/api/v1/imageMessages/bad-id"))
                .andExpect(status().isNotFound());

        verify(imageMessageService).removeById("bad-id");
    }
}
