package com.interview.saturn.repository;


import com.interview.saturn.dbo.ProductDBO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<ProductDBO, String> {

    ProductDBO findByEan(String ean);

    Long deleteByEan(String ean);
}
