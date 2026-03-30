package org.softwarecave.springbootimages.rest;

import org.softwarecave.springbootimages.bedrock.ImageGenerationException;
import org.softwarecave.springbootimages.images.model.NoSuchImageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        return createResponseEntity(pd, null, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({NoSuchImageException.class})
    public ResponseEntity<Object> handleNoSuchImageException(NoSuchImageException ex, WebRequest request) {

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        return createResponseEntity(pd, null, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({ImageGenerationException.class})
    public ResponseEntity<Object> handleNoSuchImageException(ImageGenerationException ex, WebRequest request) {

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return createResponseEntity(pd, null, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
