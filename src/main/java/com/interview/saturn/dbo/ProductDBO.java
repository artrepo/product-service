package com.interview.saturn.dbo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "products")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDBO {

    @Id
    private String id;

    @Indexed
    private String ean;

    private String name;

    private String description;

    private PriceInfoDBO priceInfo;

    /**
     * The lowest level category for the product.
     */
    private String categoryId;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
