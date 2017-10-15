package com.interview.saturn.exception;

/**
 * Created by hayrapea on 13/10/17.
 */
public class InvalidRequestDataException extends RuntimeException {

    public InvalidRequestDataException(String message) {
        super(message);
    }
}
