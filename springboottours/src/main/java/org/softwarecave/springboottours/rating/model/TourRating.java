package org.softwarecave.springboottours.rating.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.softwarecave.springboottours.client.model.Client;
import org.softwarecave.springboottours.rating.model.validation.TourRatingValue;
import org.softwarecave.springboottours.tour.model.Tour;

@Entity
@Table(name = "tour_rating")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TourRating {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tour_rating_seq")
    @SequenceGenerator(name = "tour_rating_seq", sequenceName = "tour_rating_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    @NotNull
    private Tour tour;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @NotNull
    private Client client;

    @Column(name = "comment")
    @NotBlank
    private String comment;

    @Column(name = "score")
    @TourRatingValue
    private int score;
}
