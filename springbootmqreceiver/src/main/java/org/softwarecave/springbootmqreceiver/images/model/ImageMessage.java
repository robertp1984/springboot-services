package org.softwarecave.springbootmqreceiver.images.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    private String originalFilename;

    @NotBlank
    private String contentType;

    @NotBlank
    private Instant createdTime;

    @NotNull
    private ActionType actionType;
}
