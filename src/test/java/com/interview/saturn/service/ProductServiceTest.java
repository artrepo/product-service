package com.interview.saturn.service;

import com.interview.saturn.ApplicationTest;
import com.interview.saturn.exception.AlreadyExitingRecordException;
import com.interview.saturn.exception.InvalidRequestDataException;
import com.interview.saturn.exception.ProductNotFoundException;
import com.interview.saturn.model.PriceInfo;
import com.interview.saturn.model.Product;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/** Product Integration Test using embedded test mogno */
public class ProductServiceTest extends ApplicationTest {

    @Autowired
    private ProductService productService;

    @Test
    public void productCreate() {
        String ean = "1111";
        String name= "test_product_1";
        Product product = createDummyProductWithPrice();
        product.setEan(ean);
        product.setName(name);
        productService.create(product);

        Product persistedProduct = productService.getByEan(product.getEan());

        Assert.assertNotNull(persistedProduct);
        Assert.assertEquals(persistedProduct.getName(), product.getName());
        Assert.assertEquals(persistedProduct.getEan(), product.getEan());
    }

    @Test(expected = InvalidRequestDataException.class)
    public void productCreateWithEmptyPrice() {
        String ean = "11113";
        String name= "empty pirce";
        Product product = new Product();
        product.setEan(ean);
        product.setName(name);
        productService.create(product);
    }

    @Test(expected = AlreadyExitingRecordException.class)
    public void productDoublCreate() {
        String ean = "22222";
        String name = "test_product_2";
        Product product = createDummyProductWithPrice();
        product.setEan(ean);
        product.setName(name);

        productService.create(product);

        Product persistedProduct = productService.getByEan(product.getEan());

        Assert.assertNotNull(persistedProduct);
        Assert.assertEquals(persistedProduct.getName(), product.getName());
        Assert.assertEquals(persistedProduct.getEan(), product.getEan());
        productService.create(product);
    }


    @Test
    public void productUpdate() {
        String ean = "44444";
        String name = "test_product_4";
        String other ="test_other_name4";
        Product product = createDummyProductWithPrice();
        product.setEan(ean);
        product.setName(name);
        productService.create(product);

        Product persistedProduct = productService.getByEan(ean);
        Assert.assertNotNull(persistedProduct);
        persistedProduct.setName(other);
        productService.update(persistedProduct);
        persistedProduct = productService.getByEan(ean);

        Assert.assertNotNull(persistedProduct);
        Assert.assertEquals(persistedProduct.getName(), other);
        Assert.assertNotEquals(persistedProduct.getName(), product.getName());
        Assert.assertEquals(persistedProduct.getEan(), product.getEan());
        Assert.assertNotEquals(persistedProduct.getName(), product.getName());
        Assert.assertEquals(persistedProduct.getEan(), product.getEan());
    }

    @Test(expected = InvalidRequestDataException.class)
    public void productUpdateWithEmptyEan() {
        String ean = "5555";
        String name = "test_product_5";
        Product product = createDummyProductWithPrice();
        product.setEan(ean);
        product.setName(name);

        productService.create(product);

        Product persistedProduct = productService.getByEan(ean);
        Assert.assertNotNull(persistedProduct);
        persistedProduct.setEan("");
        productService.update(persistedProduct);
    }

    @Test
    public void productUpdateWithEmptyName() {
        String ean = "66666";
        String name ="test_product_6";
        Product product = createDummyProductWithPrice();
        product.setEan(ean);
        product.setName(name);
        productService.create(product);

        Product persistedProduct = productService.getByEan(ean);
        Assert.assertNotNull(persistedProduct);
        product.setName(null);
        productService.update(persistedProduct);
        persistedProduct = productService.getByEan(ean);

        Assert.assertNotNull(persistedProduct);
        Assert.assertEquals(persistedProduct.getName(),name);
        Assert.assertEquals(persistedProduct.getEan(), ean);
    }


    @Test(expected = ProductNotFoundException.class)
    public void productDelete() {
        String ean = "77777";
        String name = "test_name_7";
        Product product = createDummyProductWithPrice();
        product.setEan(ean);
        product.setName(name);
        productService.create(product);

        Product persistedProduct = productService.getByEan(product.getEan());

        Assert.assertNotNull(persistedProduct);
        Assert.assertEquals(persistedProduct.getName(), name);
        Assert.assertEquals(persistedProduct.getEan(), ean);

        productService.delete(ean);

        productService.getByEan(ean);

    }

    @Test(expected = ProductNotFoundException.class)
    public void productDeleteNonExistingProduct() {
        String ean = "NotExistedEan";
        Product persistedProduct = productService.getByEan(ean);
        Assert.assertNull(persistedProduct);

        productService.delete(ean);
        productService.getByEan(ean);
    }

    private Product createDummyProductWithPrice(){
        Product product = new Product();
        PriceInfo priceInfo = new PriceInfo();
        priceInfo.setPrice(new BigDecimal(10));
        product.setPriceInfo(priceInfo);
        return product;
    }
}
