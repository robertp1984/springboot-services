package org.softwarecave.springboottours.rating.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.softwarecave.springboottours.rating.web.TourRatingDTO;
import org.softwarecave.springboottours.client.model.Client;
import org.softwarecave.springboottours.tour.model.Difficulty;
import org.softwarecave.springboottours.tour.model.Region;
import org.softwarecave.springboottours.tour.model.Tour;
import org.softwarecave.springboottours.tour.model.TourPackage;
import org.softwarecave.springboottours.client.service.ClientRepository;
import org.softwarecave.springboottours.tour.service.TourPackageRepository;
import org.softwarecave.springboottours.tour.service.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.softwarecave.springboottours.testutils.TestUtils.assertIsConstraintViolationException;

@SpringBootTest
public class TourRatingServiceIntegrationTest {

    public static final String OLIMP_TRIP_COMMENT = "Excellent trip. Lots of walking :)";
    
    @Autowired
    private TourRatingService tourRatingService;

    @Autowired
    private TourPackageRepository tourPackageRepository;

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private ClientRepository clientRepository;

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    public void testAddTourRating_ValidRating(int rating) {
        var client = createSampleClient();
        var tour = createSampleTour();
        var tourRatingDTO = new TourRatingDTO(tour.getCode(), client.getId(), OLIMP_TRIP_COMMENT, rating);

        tourRatingService.addTourRating(tour.getId(), tourRatingDTO);

        var ratingsByTourId = tourRatingService.findByTourId(tour.getId());
        assertTrue(ratingsByTourId.stream().anyMatch(e -> e.getComment().equals(OLIMP_TRIP_COMMENT)));
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, -5, -2, -1, 11, 12, 15, 100})
    public void testAddTourRating_NegativeRating(int rating) {
        var client = createSampleClient();
        var tour = createSampleTour();
        var tourRatingDTO = new TourRatingDTO(tour.getCode(), client.getId(), OLIMP_TRIP_COMMENT, rating);

        var e = assertThrows(Exception.class, () -> tourRatingService.addTourRating(tour.getId(), tourRatingDTO));
        assertIsConstraintViolationException(e);
    }

    private Client createSampleClient() {
        return clientRepository.save(new Client(null, "Julia", "Smith", "julia@example.com"));
    }

    private Tour createSampleTour() {
        var tourPackage = tourPackageRepository.save(new TourPackage("GR", "Greece"));
        return tourRepository.save(new Tour(null, "OL", "Olimp", "Visit Olimp in Greece", Difficulty.DIFFICULT, Region.SOUTHERN_EUROPE, tourPackage));
    }

}
