package com.interview.saturn.service;

import com.interview.saturn.dbo.CategoryDBO;
import com.interview.saturn.exception.CategoryNotFoundException;
import com.interview.saturn.exception.InvalidRequestDataException;
import com.interview.saturn.model.Category;
import com.interview.saturn.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Category CRUD service
 */
@Slf4j
@Service
public class CategoryService {

    private static final String CATEGORY_TREE_CACHE = "category-tree";
    private static final String URL_SEPARATOR = "/";

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @CacheEvict(value = CATEGORY_TREE_CACHE, allEntries = true)
    public void create(Category category) {
        log.info("Creating category: {}...", category.getName());
        String parentId = category.getParentId();
        if(parentId != null){
            CategoryDBO parent = categoryRepository.findById(parentId);
            if(parent == null){
                throw new CategoryNotFoundException("The parent category of created categoy does not exist!");
            }
        }
        CategoryDBO categoryDBO = new CategoryDBO();
        BeanUtils.copyProperties(category, categoryDBO);
        categoryRepository.save(categoryDBO);
        log.info("Category {} is created!");
    }

    @CacheEvict(value = CATEGORY_TREE_CACHE, allEntries = true)
    public void update(Category category) {
        log.info("Creating category: {}...", category.getName());
        String categoryId =category.getId();
        String parentId = category.getParentId();
        if(parentId != null){
            CategoryDBO parent = categoryRepository.findById(parentId);
            if(parent == null){
                throw new CategoryNotFoundException("The parent category of created categoy does not exist!");
            }
        }
        if(StringUtils.isEmpty(categoryId)){
            throw new InvalidRequestDataException("The category ID is missing!");
        }
        CategoryDBO categoryDBO = categoryRepository.findById(categoryId);
        BeanUtils.copyProperties(category, categoryDBO);
        categoryRepository.save(categoryDBO);
        log.info("Category {} is created!");
    }

    @Cacheable(CATEGORY_TREE_CACHE)
    public Category getById(String id) {
        log.info("Get category with id: {}", id);
        CategoryDBO categoryDBO = categoryRepository.findById(id);
        Category category = new Category();
        if(categoryDBO == null){
           throw new CategoryNotFoundException("The category is not found for id: " + id);
        }
        BeanUtils.copyProperties(categoryDBO, category);
        addChildrenRecursivly(category);

        return category;
    }

    public Category getByName(String name) {
        log.info("Get category with name: {}", name);
        CategoryDBO categoryDBO = categoryRepository.findByName(name);
        Category category = new Category();
        if(categoryDBO == null){
            throw new CategoryNotFoundException("The category is not found for name: " + name);
        }
        BeanUtils.copyProperties(categoryDBO, category);
        addChildrenRecursivly(category);

        return category;
    }

    @CacheEvict(value = CATEGORY_TREE_CACHE, allEntries = true)
    public void delete(String id) {
        log.info("Deleting category with id: {} with decendents.", id);
        CategoryDBO categoryDBO = categoryRepository.findById(id);
        List<CategoryDBO> descendants = new ArrayList<>();
        findDescendants(categoryDBO.getId(), descendants);
        categoryRepository.delete(descendants);
        categoryRepository.delete(categoryDBO);
        log.info("Deleted category with id: {} with decendents.", id);
    }


    public String buildFullUrlPath(String id){
        log.info("String building the full URL for category id: {}", id);
        CategoryDBO categoryDBO = categoryRepository.findById(id);
        if(categoryDBO == null){
            throw new CategoryNotFoundException("The category is not found for id: " + id);
        }

        List<String> fullUrlList = new ArrayList<>();
        fullUrlList.add(categoryDBO.getUrl());
        String parentId = categoryDBO.getParentId();

        //While it is not top category
        while(!StringUtils.isEmpty(parentId)){
            categoryDBO = categoryRepository.findById(parentId);
            fullUrlList.add(categoryDBO.getUrl());
            parentId=categoryDBO.getParentId();
        }
        Collections.reverse(fullUrlList);

        return URL_SEPARATOR + String.join(URL_SEPARATOR, fullUrlList);

    }

    private void addChildrenRecursivly(Category category){
        List<CategoryDBO> categoryDBOList = categoryRepository.findByParentId(category.getId());

        List<Category> children =new ArrayList<>();

        categoryDBOList.forEach(c -> {
            Category child = new Category();
            BeanUtils.copyProperties(c, child);
            children.add(child);
        });

        category.setChildren(children);
        children.forEach(this::addChildrenRecursivly);
    }

    private void findDescendants(String categoryId, List<CategoryDBO> descendants){
        List<CategoryDBO> categoryDBOList = categoryRepository.findByParentId(categoryId);
        descendants.addAll(categoryDBOList);
        categoryDBOList.forEach(c -> findDescendants(c.getId(), descendants));
    }
}
