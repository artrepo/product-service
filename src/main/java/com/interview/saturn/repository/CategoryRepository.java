package com.interview.saturn.repository;

import com.interview.saturn.dbo.CategoryDBO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Category Repository
 */
public interface CategoryRepository  extends MongoRepository<CategoryDBO, String> {

    CategoryDBO findById(String id);

    CategoryDBO findByName(String name);

    Long deleteById(String id);

    List<CategoryDBO> findByParentId(String parentId);

}
