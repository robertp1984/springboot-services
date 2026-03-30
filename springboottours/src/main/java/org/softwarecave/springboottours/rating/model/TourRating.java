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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.softwarecave.springboottours.client.model.Client;
import org.softwarecave.springboottours.tour.model.Tour;
import org.softwarecave.springboottours.rating.model.validation.TourRatingValue;

@Entity
@Table(name = "TOUR_RATING")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourRating {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tour_rating_seq")
    @SequenceGenerator(name = "tour_rating_seq", sequenceName = "tour_rating_seq", allocationSize = 1)
    @EqualsAndHashCode.Exclude
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    private Tour tour;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    private Client client;

    @Column(name = "comment")
    @NotNull
    @NotBlank
    private String comment;

    @Column(name = "score")
    @TourRatingValue
    private int score;
}
