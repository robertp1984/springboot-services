package org.softwarecave.springboottours.rating.web;

import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.softwarecave.springboottours.client.model.Client;
import org.softwarecave.springboottours.rating.model.TourRating;
import org.softwarecave.springboottours.rating.service.TourRatingService;
import org.softwarecave.springboottours.rating.web.converter.TourRatingDTOConverter;
import org.softwarecave.springboottours.tour.model.Difficulty;
import org.softwarecave.springboottours.tour.model.NoSuchTourException;
import org.softwarecave.springboottours.tour.model.Region;
import org.softwarecave.springboottours.tour.model.Tour;
import org.softwarecave.springboottours.tour.model.TourPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TourRatingController.class)
public class TourRatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TourRatingService tourRatingService;

    @MockitoBean(answers = Answers.CALLS_REAL_METHODS)
    private TourRatingDTOConverter tourRatingDTOConverter;

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    public void testAddTourRating_Valid() throws Exception {
        Long tourId = 5L;
        String comment = "Nice tour. I give it five stars";
        int rating = 5;
        TourRatingDTO tourRatingDTO = new TourRatingDTO("tourCode", 6L, comment, rating);

        TourRating createdTourRating = new TourRating(10L, createDummyTour(tourId, "tourCode"), createDummyClient(), comment, rating);
        when(tourRatingService.addTourRating(tourId, tourRatingDTO))
                .thenReturn(createdTourRating);

        mockMvc.perform(post("/api/v1/tours/{tourId}/ratings", tourId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonMapper.writeValueAsString(tourRatingDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tourCode").value("tourCode"))
                .andExpect(jsonPath("$.clientId").value(6L))
                .andExpect(jsonPath("$.comment").value(comment))
                .andExpect(jsonPath("$.rating").value(rating));

        verify(tourRatingService).addTourRating(tourId, tourRatingDTO);
        verifyNoMoreInteractions(tourRatingService);
    }

    @Test
    public void testAddTourRating_TourNotFound() throws Exception {
        Long tourId = 5L;
        String comment = "Nice tour. I give it five stars";
        int rating = 5;
        TourRatingDTO tourRatingDTO = new TourRatingDTO("tourCode", 6L, comment, rating);

        when(tourRatingService.addTourRating(tourId, tourRatingDTO))
                .thenThrow(new NoSuchTourException("Tour not found"));

        mockMvc.perform(post("/api/v1/tours/{tourId}/ratings", tourId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonMapper.writeValueAsString(tourRatingDTO)))
                .andExpect(status().isNotFound());

        verify(tourRatingService).addTourRating(tourId, tourRatingDTO);
        verifyNoMoreInteractions(tourRatingService);
    }

    @Test
    public void testAddTourRating_CommentBlank() throws Exception {
        Long tourId = 5L;
        String comment = "";
        int rating = 5;
        TourRatingDTO tourRatingDTO = new TourRatingDTO("tourCode", 6L, comment, rating);

        mockMvc.perform(post("/api/v1/tours/{tourId}/ratings", tourId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonMapper.writeValueAsString(tourRatingDTO)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(tourRatingService);
    }

    @Test
    public void testAddTourRating_RatingTooHigh() throws Exception {
        Long tourId = 5L;
        String comment = "";
        int rating = 11;
        TourRatingDTO tourRatingDTO = new TourRatingDTO("tourCode", 6L, comment, rating);

        mockMvc.perform(post("/api/v1/tours/{tourId}/ratings", tourId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonMapper.writeValueAsString(tourRatingDTO)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(tourRatingService);
    }

    @Test
    public void testGetTourRatings_Multiple() throws Exception {
        Long tourId = 5L;

        TourRating tourRating1 = new TourRating(10L, createDummyTour(tourId, "TC1"), createDummyClient(), "Good", 4);
        TourRating tourRating2 = new TourRating(12L, createDummyTour(tourId, "TC2"), createDummyClient(), "Very nice", 6);

        when(tourRatingService.findByTourId(tourId))
                .thenReturn(List.of(tourRating1, tourRating2));

        mockMvc.perform(get("/api/v1/tours/{tourId}/ratings", tourId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tourCode").value("TC1"))
                .andExpect(jsonPath("$[0].clientId").value(6L))
                .andExpect(jsonPath("$[0].comment").value("Good"))
                .andExpect(jsonPath("$[0].rating").value(4))
                .andExpect(jsonPath("$[1].tourCode").value("TC2"))
                .andExpect(jsonPath("$[1].clientId").value(6L))
                .andExpect(jsonPath("$[1].comment").value("Very nice"))
                .andExpect(jsonPath("$[1].rating").value(6));

        verify(tourRatingService).findByTourId(tourId);
        verifyNoMoreInteractions(tourRatingService);
    }

    @Test
    public void testGetTourRatings_One() throws Exception {
        Long tourId = 5L;

        TourRating tourRating1 = new TourRating(10L, createDummyTour(tourId, "TC1"), createDummyClient(), "Good", 4);

        when(tourRatingService.findByTourId(tourId))
                .thenReturn(List.of(tourRating1));

        mockMvc.perform(get("/api/v1/tours/{tourId}/ratings", tourId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tourCode").value("TC1"))
                .andExpect(jsonPath("$[0].clientId").value(6L))
                .andExpect(jsonPath("$[0].comment").value("Good"))
                .andExpect(jsonPath("$[0].rating").value(4));

        verify(tourRatingService).findByTourId(tourId);
        verifyNoMoreInteractions(tourRatingService);
    }

    @Test
    public void testGetTourRatings_Zero() throws Exception {
        Long tourId = 5L;

        when(tourRatingService.findByTourId(tourId))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/tours/{tourId}/ratings", tourId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tourCode").doesNotHaveJsonPath());

        verify(tourRatingService).findByTourId(tourId);
        verifyNoMoreInteractions(tourRatingService);
    }

    private static Tour createDummyTour(Long tourId, String tourCode) {
        return new Tour(tourId, tourCode, "Tour name", "Tour desc", Difficulty.VARIES, Region.CENTRAL_COAST,
                createDummyTourPackage());
    }

    private static Client createDummyClient() {
        return new Client(6L, "Joe", "Blue", "joe@blue.aa");
    }

    private static TourPackage createDummyTourPackage() {
        return new TourPackage(10L, "TPAC", "Tour package name");
    }


}
