package org.softwarecave.springboottours.rating.service;

import lombok.RequiredArgsConstructor;
import org.softwarecave.springboottours.client.model.Client;
import org.softwarecave.springboottours.client.model.NoSuchClientException;
import org.softwarecave.springboottours.client.service.ClientRepository;
import org.softwarecave.springboottours.rating.model.TourRating;
import org.softwarecave.springboottours.rating.model.TourRatingValidationException;
import org.softwarecave.springboottours.rating.web.TourRatingDTO;
import org.softwarecave.springboottours.rating.web.converter.TourRatingDTOConverter;
import org.softwarecave.springboottours.tour.model.NoSuchTourException;
import org.softwarecave.springboottours.tour.model.Tour;
import org.softwarecave.springboottours.tour.model.TourValidationException;
import org.softwarecave.springboottours.tour.service.TourRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TourRatingService {

    private final TourRatingRepository tourRatingRepository;
    private final TourRepository tourRepository;
    private final ClientRepository clientRepository;

    public TourRating addTourRating(Long tourId, TourRatingDTO tourRatingDTO) {
        if (tourId == null) {
            throw new TourValidationException("Tour id must not be null");
        }
        if (tourRatingDTO == null) {
            throw new TourRatingValidationException("Tour rating must not be null");
        }
        if (tourRatingDTO.getClientId() == null) {
            throw new TourRatingValidationException("Tour rating client id must not be null");
        }

        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new NoSuchTourException("Tour with id=%s not found".formatted(tourId)));
        Client client = clientRepository.findById(tourRatingDTO.getClientId())
                .orElseThrow(() -> new NoSuchClientException("Client with id=%d not found".formatted(tourRatingDTO.getClientId())));

        TourRating tourRating = new TourRating(null, tour, client,
                tourRatingDTO.getComment(), tourRatingDTO.getRating());
        return tourRatingRepository.save(tourRating);
    }

    @Transactional(readOnly = true)
    public List<TourRating> findByTourId(Long tourId) {
        if (tourId == null) {
            throw new TourRatingValidationException("Tour id must not be null");
        }

        return tourRatingRepository.findByTourId(tourId);
    }
}
