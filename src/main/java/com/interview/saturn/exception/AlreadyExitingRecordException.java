package com.interview.saturn.exception;

/**
 * Exception during creation of existing record
 */
public class AlreadyExitingRecordException extends RuntimeException {

    public AlreadyExitingRecordException(String message) {
            super(message);
    }
}
