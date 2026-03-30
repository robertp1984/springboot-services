package org.softwarecave.springbootmqreceiver.images.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "events")
public class ImageMessage {
    @Id
    private String id;
    private String originalFilename;
    private String contentType;
    private Instant createdTime;
    private ActionType actionType;
}
