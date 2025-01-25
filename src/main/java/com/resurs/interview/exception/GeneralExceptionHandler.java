package com.resurs.interview.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GeneralExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralExceptionHandler.class);

    @ExceptionHandler(exception = DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(exception = Exception.class)
    public ResponseEntity<String> handleGeneralException(final Exception ex, final HttpServletRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.badRequest().build();
    }
}
