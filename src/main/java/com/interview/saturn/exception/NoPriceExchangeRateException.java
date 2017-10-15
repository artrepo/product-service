package com.interview.saturn.exception;

/**
 * Proce Exchange rate can't be found exception
 */
public class NoPriceExchangeRateException extends RuntimeException {

    public NoPriceExchangeRateException(String message) {
        super(message);
    }
}
