package com.interview.saturn.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * PriceInfo model of the product
 */
@Data
public class PriceInfo implements Serializable{

    private String currency;

    /** The actual current price of the product*/
    @NotNull(message = "Price must not be null")
    private BigDecimal price;

    //TODO can be also other prices info like before deiscount price, unit price etc.
}
