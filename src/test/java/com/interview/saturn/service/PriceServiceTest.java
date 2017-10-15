package com.interview.saturn.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

public class PriceServiceTest{

    @Mock
    private PriceExchangeRateClient priceExchangeRateClient;

    private PriceService priceService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        priceService = new PriceService(priceExchangeRateClient);
    }

    @Test
    public void getExchangeTest() {
        Mockito.when(priceExchangeRateClient.getExchangeRate(Mockito.any(), Mockito.eq("USD"))).thenReturn(new BigDecimal(15));

        BigDecimal newPrice = priceService.exchange(new BigDecimal(2), "USD");

        Assert.assertEquals(newPrice, new BigDecimal(30));
    }

    @Test
    public void getExchangeCurrencyEmptyTest() {
        BigDecimal amount =   new BigDecimal(2);

        BigDecimal exchanged = priceService.exchange(amount, null);
        Assert.assertEquals(amount, exchanged);

        exchanged = priceService.exchange(amount, "");
        Assert.assertEquals(amount, exchanged);
    }
}
