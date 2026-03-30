package org.softwarecave.springboottours.tour.model;

public class NoSuchTourException extends RuntimeException {
    public NoSuchTourException(String message) {
        super(message);
    }
}
