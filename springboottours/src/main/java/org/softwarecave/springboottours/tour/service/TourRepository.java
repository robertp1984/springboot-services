package org.softwarecave.springboottours.tour.service;

import org.softwarecave.springboottours.tour.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TourRepository extends JpaRepository<Tour, Long> {

    Optional<Tour> findByCode(String code);
}
