package com.interview.saturn.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Product Price Info for DB
 */
@Data
@AllArgsConstructor
public class PriceInfoDBO {

    /** The actual current price of the product*/
    private BigDecimal price;
}
