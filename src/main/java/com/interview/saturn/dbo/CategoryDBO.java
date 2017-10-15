package com.interview.saturn.dbo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Category DB object
 */
@Document(collection = "categories")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CategoryDBO {
    @Id
    private String id;

    private String name;

    private String url;

    @Indexed
    private String parentId;

    private int order;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
