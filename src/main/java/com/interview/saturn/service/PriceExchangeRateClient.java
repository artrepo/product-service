package com.interview.saturn.service;

import com.interview.saturn.exception.NoPriceExchangeRateException;
import com.interview.saturn.model.ExchangeRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Slf4j
@Component
public class PriceExchangeRateClient {

    @Value("${price.exchange.api.url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     *The exchange rate. NOTE: it might be cached for some time  depending on the requirements
     *
     * @param fromCurrency currency from which the amount should be changed
     * @param toCurrency currency to which the amount should be changed
     * @return the rate
     */
    public BigDecimal getExchangeRate(@NotNull String fromCurrency, @NotNull String toCurrency) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl).path("latest").
                queryParam("fromCurrency", fromCurrency).
                queryParam("symbols", toCurrency).
                build().toString();
        ExchangeRate exchangeRate = restTemplate.getForObject(url, ExchangeRate.class);
        if(exchangeRate == null || CollectionUtils.isEmpty(exchangeRate.getRates())){
            throw new NoPriceExchangeRateException("There is no exchange rate for " + toCurrency);
        }
        log.info("Exchange rate: {}", exchangeRate);
        return BigDecimal.valueOf(exchangeRate.getRates().get(toCurrency));
    }
}
