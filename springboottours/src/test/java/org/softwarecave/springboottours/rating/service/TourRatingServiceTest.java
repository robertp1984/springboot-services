package org.softwarecave.springboottours.rating.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwarecave.springboottours.rating.web.TourRatingDTO;
import org.softwarecave.springboottours.client.model.Client;
import org.softwarecave.springboottours.tour.model.Tour;
import org.softwarecave.springboottours.rating.model.TourRating;
import org.softwarecave.springboottours.client.service.ClientRepository;
import org.softwarecave.springboottours.tour.model.NoSuchTourException;
import org.softwarecave.springboottours.tour.service.TourRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class TourRatingServiceTest {

    @InjectMocks
    private TourRatingService tourRatingService;

    @Mock
    private TourRepository tourRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private TourRatingRepository tourRatingRepository;

    @Test
    public void testAddTourRating_NoSuchTourException() {
        long id = 10000L;
        doReturn(Optional.empty()).when(tourRepository).findById(id);

        Client client = new Client(1L, "Joe", "Doe", "joe@example.com");
        assertThrows(NoSuchTourException.class, () -> tourRatingService.addTourRating(id, createDummyTourRatingDTO(client)));

        verify(tourRepository, times(1)).findById(id);
        verifyNoInteractions(tourRatingRepository);
    }

    @Test
    public void testAddTourRating_Success() {
        long id = 1000L;

        Client client = new Client(1L, "Joe", "Doe", "joe@example.com");
        TourRatingDTO tourRatingDTO = createDummyTourRatingDTO(client);
        Tour tour = new Tour();
        doReturn(Optional.of(tour)).when(tourRepository).findById(id);
        doReturn(Optional.of(client)).when(clientRepository).findById(tourRatingDTO.getClientId());
        doReturn(null).when(tourRatingRepository).save(any(TourRating.class));

        tourRatingService.addTourRating(id, tourRatingDTO);

        ArgumentCaptor<TourRating> tourRatingArgumentCaptor = ArgumentCaptor.forClass(TourRating.class);
        verify(tourRepository, times(1)).findById(id);
        verify(clientRepository, times(1)).findById(client.getId());
        verify(tourRatingRepository, times(1)).save(tourRatingArgumentCaptor.capture());
        assertThat(tourRatingArgumentCaptor.getValue())
                .hasFieldOrPropertyWithValue("client.id", tourRatingDTO.getClientId())
                .hasFieldOrPropertyWithValue("comment", tourRatingDTO.getComment())
                .hasFieldOrPropertyWithValue("score", tourRatingDTO.getRating());
    }

    private TourRatingDTO createDummyTourRatingDTO(Client client) {
        TourRatingDTO dto = new TourRatingDTO();
        dto.setTourCode("AA");
        dto.setClientId(client.getId());
        dto.setComment("Great tour");
        dto.setRating(5);
        return dto;
    }
}
