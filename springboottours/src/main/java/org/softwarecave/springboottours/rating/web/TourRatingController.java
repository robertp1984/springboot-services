package org.softwarecave.springboottours.rating.web;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.softwarecave.springboottours.rating.service.TourRatingService;
import org.softwarecave.springboottours.rating.web.converter.TourRatingDTOConverter;
import org.softwarecave.springboottours.rating.model.TourRating;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/tours/{tourId}/ratings")
@Slf4j
public class TourRatingController {
    private final TourRatingService tourRatingService;

    public TourRatingController(TourRatingService tourRatingService) {
        this.tourRatingService = tourRatingService;
    }

    @PostMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TourRatingDTO addTourRating(@RequestBody @Valid TourRatingDTO tourRatingDTO,
                                        @PathVariable("tourId") Long tourId) {
        log.info("Called addTourRating for tourId={} with {}", tourId, tourRatingDTO);
        TourRating added = tourRatingService.addTourRating(tourId, tourRatingDTO);
        return new TourRatingDTOConverter().convertToDTO(added);
    }

    @GetMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TourRatingDTO> getTourRatings(@PathVariable("tourId") Long tourId) {
        return tourRatingService.findByTourId(tourId);
    }
}
