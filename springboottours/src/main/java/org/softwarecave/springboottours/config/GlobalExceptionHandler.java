package org.softwarecave.springboottours.config;

import org.softwarecave.springboottours.client.model.NoSuchClientException;
import org.softwarecave.springboottours.rating.model.TourRatingValidationException;
import org.softwarecave.springboottours.tour.model.NoSuchTourException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        return  createResponseEntity(pd, null, HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleNoSuchTourException(NoSuchTourException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Tour not found");
        return  createResponseEntity(pd, null, HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleNoSuchClientException(NoSuchClientException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Client not found");
        return  createResponseEntity(pd, null, HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleNoSuchTourException(TourRatingValidationException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Bad tour rating request");
        return  createResponseEntity(pd, null, HttpStatus.BAD_REQUEST, null);
    }

}
