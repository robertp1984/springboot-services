package org.softwarecave.springboottours.rating.web.converter;

import org.softwarecave.springboottours.rating.model.TourRating;
import org.softwarecave.springboottours.rating.web.TourRatingDTO;

public class TourRatingDTOConverter {
    public TourRatingDTO convertToDTO(TourRating tourRating) {
        return new TourRatingDTO(tourRating.getTour().getCode(), tourRating.getClient().getId(), tourRating.getComment(), tourRating.getScore());
    }

}
