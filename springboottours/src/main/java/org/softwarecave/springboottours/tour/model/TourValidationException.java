package org.softwarecave.springboottours.tour.model;

public class TourValidationException extends RuntimeException {
    public TourValidationException(String message) {
        super(message);
    }
}
