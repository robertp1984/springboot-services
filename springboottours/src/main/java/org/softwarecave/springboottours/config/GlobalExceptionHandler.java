package org.softwarecave.springboottours.config;

import org.softwarecave.springboottours.client.model.NoSuchClientException;
import org.softwarecave.springboottours.tour.model.NoSuchTourException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        return  createResponseEntity(pd, null, HttpStatus.NOT_FOUND, request);
    }

    public ResponseEntity<Object> handleNoSuchTourException(NoSuchTourException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        return  createResponseEntity(pd, null, HttpStatus.NOT_FOUND, request);
    }

    public ResponseEntity<Object> handleNoSuchClientException(NoSuchClientException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        return  createResponseEntity(pd, null, HttpStatus.NOT_FOUND, request);
    }

}
