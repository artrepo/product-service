package com.interview.saturn.service;

import com.interview.saturn.ApplicationTest;
import com.interview.saturn.exception.CategoryNotFoundException;
import com.interview.saturn.exception.InvalidRequestDataException;
import com.interview.saturn.model.Category;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test for category CRUD service will use embeded Mongo for the test
 */
public class CategoryServiceTest extends ApplicationTest{

    @Autowired
    private CategoryService categoryService;

    @Test
    public void createTopCategory(){
        Category category = createTopCategory("Toys", "toys");
        categoryService.create(category);
        Category persistedCategory = categoryService.getByName(category.getName());

        Assert.assertNotNull(persistedCategory);
        Assert.assertEquals(persistedCategory.getName(), category.getName());
        Assert.assertEquals(persistedCategory.getUrl(), category.getUrl());
        Assert.assertNull(persistedCategory.getParentId());
    }

    @Test
    public void createChildCategory(){
        Category parent = createTopCategory("Cars", "cars");
        categoryService.create(parent);

        Category persistedParentCategory = categoryService.getByName(parent.getName());

        Assert.assertNotNull(persistedParentCategory);
        Assert.assertEquals(persistedParentCategory.getName(), parent.getName());
        Assert.assertEquals(persistedParentCategory.getUrl(), parent.getUrl());
        Assert.assertNull(persistedParentCategory.getParentId());


        Category child = createChildCategory("Track", "track", persistedParentCategory.getId());
        categoryService.create(child);

        Category persistedChildCategory = categoryService.getByName(child.getName());

        Assert.assertNotNull(persistedChildCategory);
        Assert.assertEquals(persistedChildCategory.getName(), child.getName());
        Assert.assertEquals(persistedChildCategory.getUrl(), child.getUrl());
        Assert.assertNotNull(persistedChildCategory.getParentId());
    }

    @Test(expected = CategoryNotFoundException.class)
    public void createChildNotExistingParentCategory(){
        Category child = createChildCategory("Child", "child", "XXXXX" );
        categoryService.create(child);
    }

    @Test
    public void updateCategory(){

        Category cats = createTopCategory("Cats", "cats");
        categoryService.create(cats);
        Category persistedCats = categoryService.getByName(cats.getName());

        Assert.assertNotNull(persistedCats);
        Assert.assertEquals(persistedCats.getName(), cats.getName());
        Assert.assertEquals(persistedCats.getUrl(), cats.getUrl());
        Assert.assertNull(persistedCats.getParentId());

        Category animal = createTopCategory("Annimal", "animal");
        categoryService.create(animal);
        Category persistedAnimal = categoryService.getByName(animal.getName());

        Assert.assertNotNull(persistedAnimal);
        Assert.assertEquals(persistedAnimal.getName(), animal.getName());
        Assert.assertEquals(persistedAnimal.getUrl(), animal.getUrl());
        Assert.assertNull(persistedAnimal.getParentId());

        Category childCats = createChildCategory("White Cats", "white-cats", persistedCats.getId());
        categoryService.create(childCats);
        Category persistedChild = categoryService.getByName(childCats.getName());

        Assert.assertNotNull(persistedChild);
        Assert.assertEquals(persistedChild.getName(), childCats.getName());
        Assert.assertEquals(persistedChild.getUrl(), childCats.getUrl());
        Assert.assertNotNull(persistedChild.getParentId());


        Category upadtedChild = createUpdateCategory(persistedChild.getId(),"Yellow Cats", "yellow-cats", animal.getId());
        categoryService.update(upadtedChild);
        Category persistedUpdatedChild = categoryService.getByName(upadtedChild.getName());

        Assert.assertNotNull(persistedUpdatedChild);
        Assert.assertEquals(persistedUpdatedChild.getName(), upadtedChild.getName());
        Assert.assertEquals(persistedUpdatedChild.getUrl(), upadtedChild.getUrl());
        Assert.assertEquals(persistedUpdatedChild.getParentId(), upadtedChild.getParentId());

    }

    @Test(expected = InvalidRequestDataException.class)
    public void updateWithEmptyCategoryId() {
        Category cats = createTopCategory("Dogs", "dogs");
        categoryService.update(cats);
    }

    @Test(expected = CategoryNotFoundException.class)
    public void deleteCategory(){
        Category category = createTopCategory("Tigers", "tigers");
        categoryService.create(category);
        Category persistedCategory = categoryService.getByName(category.getName());

        Assert.assertNotNull(persistedCategory);
        Assert.assertEquals(persistedCategory.getName(), category.getName());
        Assert.assertEquals(persistedCategory.getUrl(), category.getUrl());
        Assert.assertNull(persistedCategory.getParentId());

        categoryService.delete(persistedCategory.getId());

        persistedCategory = categoryService.getByName(category.getName());
        Assert.assertNull(persistedCategory);
    }

    @Test
    public void  buildFullUrlPath(){
        Category parent = createTopCategory("Fruts", "fruts");
        categoryService.create(parent);

        Category persistedParentCategory = categoryService.getByName(parent.getName());

        Assert.assertNotNull(persistedParentCategory);
        Assert.assertEquals(persistedParentCategory.getName(), parent.getName());
        Assert.assertEquals(persistedParentCategory.getUrl(), parent.getUrl());
        Assert.assertNull(persistedParentCategory.getParentId());


        Category child = createChildCategory("Dry Fruts", "dry-fruits", persistedParentCategory.getId());
        categoryService.create(child);

        Category persistedChildCategory = categoryService.getByName(child.getName());

        Assert.assertNotNull(persistedChildCategory);
        Assert.assertEquals(persistedChildCategory.getName(), child.getName());
        Assert.assertEquals(persistedChildCategory.getUrl(), child.getUrl());
        Assert.assertNotNull(persistedChildCategory.getParentId());
        String urlPath = categoryService.buildFullUrlPath(persistedChildCategory.getId());
        Assert.assertEquals(urlPath, "/" + parent.getUrl() + "/" + child.getUrl());
    }

    private Category createTopCategory(String name, String url){
        Category category = new Category();
        category.setName(name);
        category.setUrl(url);
        return category;
    }

    private Category createChildCategory(String name, String url, String parentId){
        Category category = createTopCategory(name, url);
        category.setParentId(parentId);
        return category;
    }

    private Category createUpdateCategory(String id, String name, String url, String parentId){
        Category category = createChildCategory(name, url, parentId);
        category.setId(id);

        return category;
    }
}
