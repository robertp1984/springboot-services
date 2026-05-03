package org.softwarecave.springboottours.rating.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.softwarecave.springboottours.rating.model.validation.TourRatingValue;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourRatingDTO {
    @NotBlank
    private String tourCode;

    @NotNull
    private Long clientId;

    @NotBlank
    private String comment;

    @TourRatingValue
    private int rating;
}
