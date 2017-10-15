package com.interview.saturn.model;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class ExchangeRate {

    private Date date;
    private String base;
    private Map<String, Double> rates;
}
