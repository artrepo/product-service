package com.interview.saturn.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * Category info model
 */
@Data
public class Category implements Serializable{

    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String url;

    private String parentId;

    private List<Category> children;
}
