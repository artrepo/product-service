package com.interview.saturn.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Slf4j
@Service
public class PriceService {

    @Value("${target.currency}")
    private String currency;

    private final PriceExchangeRateClient priceExchangeRateClient;

    @Autowired
    public PriceService(PriceExchangeRateClient priceExchangeRateClient) {
        this.priceExchangeRateClient = priceExchangeRateClient;
    }

    public BigDecimal exchange(@NonNull BigDecimal amount, String exchangeCurrency) {
        if (StringUtils.isEmpty(exchangeCurrency) || exchangeCurrency.equals(currency)) {
            log.info("No currency is provided");
            return amount;
        }

        BigDecimal rate = priceExchangeRateClient.getExchangeRate(currency, exchangeCurrency);

        return rate.multiply(amount);
    }
}
