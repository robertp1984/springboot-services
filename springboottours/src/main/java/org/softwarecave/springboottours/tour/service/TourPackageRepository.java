package org.softwarecave.springboottours.tour.service;

import org.softwarecave.springboottours.tour.model.TourPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TourPackageRepository extends JpaRepository<TourPackage, String> {
    Optional<TourPackage> findByCode(String code);
}
