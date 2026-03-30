package org.softwarecave.springboottours.rating.service;

import org.softwarecave.springboottours.rating.model.TourRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourRatingRepository extends JpaRepository<TourRating,Long> {
    public List<TourRating> findByTourId(Long tourId);
}
