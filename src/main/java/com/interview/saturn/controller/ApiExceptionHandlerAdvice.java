package com.interview.saturn.controller;

import com.interview.saturn.exception.AlreadyExitingRecordException;
import com.interview.saturn.exception.InvalidRequestDataException;
import com.interview.saturn.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandlerAdvice {

    @ExceptionHandler({ProductNotFoundException.class})
    public ResponseEntity<String> notFound(RuntimeException re) {
        log.warn("Product not found. ", re);
        return new ResponseEntity<>(re.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InvalidRequestDataException.class, AlreadyExitingRecordException.class})
    public ResponseEntity<String> badRequest(RuntimeException re) {
        log.warn("The request data is invalid. ", re);
        return new ResponseEntity<>(re.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<String> handleRunTimeException(RuntimeException re) {
        log.error("Unexpected exception. ", re);
        return new ResponseEntity<>(re.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
