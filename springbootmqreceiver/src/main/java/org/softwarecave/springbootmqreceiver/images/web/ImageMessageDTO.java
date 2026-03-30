package org.softwarecave.springbootmqreceiver.images.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.softwarecave.springbootmqreceiver.images.model.ActionType;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageMessageDTO {
    private String id;
    private String originalFilename;
    private String contentType;
    private Instant createdTime;
    private ActionType actionType;
}
