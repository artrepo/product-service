package com.interview.saturn.exception;

/**
 * Thrown whn the category can't be found
 */
public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
