package com.interview.saturn.service;

import com.interview.saturn.ApplicationTest;
import com.interview.saturn.exception.NoPriceExchangeRateException;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ExchangeRateClientIT extends ApplicationTest {

    @Autowired
    private PriceExchangeRateClient priceExchangeRateClient;

    @Test
    public void getExchange() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(priceExchangeRateClient.getExchangeRate("EUR", "USD")).
                as("The rate should be existing.").isNotNull();
        softAssertions.assertAll();
    }

    @Test(expected = NoPriceExchangeRateException.class)
    public void getExchangeWithWrongToCurrency() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(priceExchangeRateClient.getExchangeRate("EUR", "XXX")).
                as("The rate should be existing.").isNotNull();
        softAssertions.assertAll();
    }

    @Test(expected = NoPriceExchangeRateException.class)
    public void getExchangeWithWrongFromCurrency() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(priceExchangeRateClient.getExchangeRate("XXX", "EUR")).
                as("The rate should be existing.").isNotNull();
        softAssertions.assertAll();
    }
}
