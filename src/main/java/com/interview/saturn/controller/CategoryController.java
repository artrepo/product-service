package com.interview.saturn.controller;

import com.interview.saturn.model.Category;
import com.interview.saturn.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Category Contorller for CRUD operations
 */
@RestController
@RequestMapping(value = "/rest/api/category", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {

    public static final String POST_CATEGORY_CREATE_PATH = "/create";
    public static final String PUT_CATEGORY_CREATE_PATH =  "/update/";
    public static final String GET_CATEGORY_BY_ID_PATH =  "/id/{id}";
    public static final String GET_CATEGORY_BY_NAME_PATH =  "/name/{name}";
    public static final String DELETE_CATEGORY_BY_ID_PATH = "/delete/{id}";

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ApiOperation("Create a new category")
    @PostMapping(path = POST_CATEGORY_CREATE_PATH)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Category is successfully created!")
    })
    public ResponseEntity<String> createCategory(@Valid @RequestBody Category category) {
        categoryService.create(category);
        return ResponseEntity.ok("The category is created!");
    }

    @ApiOperation("Update the category")
    @PutMapping(path = PUT_CATEGORY_CREATE_PATH)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid id"),
            @ApiResponse(code = 200, message = "Category is successfully created!")

    })
    public ResponseEntity<String> updateCategory(@RequestBody Category category) {
        categoryService.update(category);
        return ResponseEntity.ok("The category is updated!");
    }

    @ApiOperation("Get the category by ean")
    @GetMapping(path = GET_CATEGORY_BY_ID_PATH)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The category is not found!")
    })
    public Category getCategoryById(@PathVariable String id) {
        return categoryService.getById(id);
    }

    @ApiOperation("Get the category by ean")
    @GetMapping(path = GET_CATEGORY_BY_NAME_PATH)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The category is not found!")
    })
    public Category getCategoryByName(@PathVariable String name) {
        return categoryService.getByName(name);
    }


    @ApiOperation("Get the category by ean")
    @DeleteMapping(path = DELETE_CATEGORY_BY_ID_PATH)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The category is not existed anymore!")
    })
    public ResponseEntity<String>  deleteCategory(@PathVariable String ean) {
        categoryService.delete(ean);
        return ResponseEntity.ok("The category is deleted!");
    }
}
