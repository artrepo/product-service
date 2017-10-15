package com.interview.saturn.controller;

import com.interview.saturn.model.Product;
import com.interview.saturn.service.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Product CRUD controller
 */
@RestController
@RequestMapping(value = "/rest/api/product", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    private static final String POST_PRODUCT_CREATE_PATH = "/create";
    private static final String PUT_PRODUCT_CREATE_PATH =  "/update/";
    private static final String GET_PRODUCT_BY_EAN_PATH =  "/{ean}";
    private static final String DELETE_PRODUCT_BY_EAN_PATH = "/delete/{ean}";
    private static final String GET_PRODUCT_CATEGORY_URL_BY_EAN_PATH =  "/categoryUrl/{ean}";

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation("Create a new product")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid product data is provided"),
            @ApiResponse(code = 200, message = "Product is successfully created!")
    })
    @PostMapping(path = POST_PRODUCT_CREATE_PATH)
    public ResponseEntity<String> createProduct(@Valid @RequestBody Product product) {
        productService.create(product);
        return ResponseEntity.ok("The product is created!");
    }

    @ApiOperation("Update the product")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid product data is provided"),
            @ApiResponse(code = 200, message = "Product is successfully created!")

    })
    @PutMapping(path = PUT_PRODUCT_CREATE_PATH)
    public ResponseEntity<String> updateProduct(@RequestBody Product product) {
        productService.update(product);
        return ResponseEntity.ok("The product is updated!");
    }

    @ApiOperation("Get the product by ean")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The product is not found!")
    })
    @GetMapping(path = GET_PRODUCT_BY_EAN_PATH)
    public ResponseEntity<Product> getProduct(@PathVariable String ean) {
        return ResponseEntity.ok(productService.getByEan(ean));
    }

    @ApiOperation("Get the product by ean")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The product is not exist anymore!")
    })
    @DeleteMapping(path = DELETE_PRODUCT_BY_EAN_PATH)
    public ResponseEntity<String>  deleteProduct(@PathVariable String ean) {
        productService.delete(ean);
        return ResponseEntity.ok("The product is deleted!");
    }

    @ApiOperation("Get the product by ean")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The product is not found!")
    })
    @GetMapping(path = GET_PRODUCT_CATEGORY_URL_BY_EAN_PATH)
    public ResponseEntity<String> getProductCategoryUrl(@PathVariable String ean) {
        return ResponseEntity.ok(productService.getProductCategoryUrl(ean));
    }

}
