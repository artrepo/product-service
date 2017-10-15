package com.interview.saturn.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class Product implements Serializable {

    @NotBlank(message = "Ena must not be empty")
    private String ean;

    @NotBlank(message = "Name must not be empty")
    private String name;

    private String description;

    @NotNull(message = "PriceInfo must not be null")
    private PriceInfo priceInfo;

    /**
     * The lowest level of category for the product.
     */
    @NotBlank(message = "Name must not be empty")
    private String categoryId;

}
