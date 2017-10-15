package com.interview.saturn.service;

import com.interview.saturn.dbo.PriceInfoDBO;
import com.interview.saturn.dbo.ProductDBO;
import com.interview.saturn.exception.AlreadyExitingRecordException;
import com.interview.saturn.exception.InvalidRequestDataException;
import com.interview.saturn.exception.ProductNotFoundException;
import com.interview.saturn.model.PriceInfo;
import com.interview.saturn.model.Product;
import com.interview.saturn.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final PriceService priceService;
    private final CategoryService categoryService;

    @Autowired
    public ProductService(ProductRepository productRepository, PriceService priceService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.priceService = priceService;
        this.categoryService = categoryService;
    }

    public void create(Product product) {
        log.info("Creating product {}", product);
        final String ean = product.getEan();

        if(productRepository.findByEan(ean) != null){
            throw new AlreadyExitingRecordException("The product wit ean " + ean +" have been already created");
        }

        ProductDBO productDBO = new ProductDBO();
        BeanUtils.copyProperties(product, productDBO);
        setPriceInfo(product.getPriceInfo(),productDBO);

        productRepository.save(productDBO);
        log.info("Product is created {}", product);
    }

    public void update(Product product) {
        log.info("Update product {}", product);
        final String ean = product.getEan();

        if(StringUtils.isEmpty(product.getEan())){
            log.error("The request parameter does not contain the ean.");
            throw new InvalidRequestDataException("The ean should not be empty!");
        }

        ProductDBO productDBO = productRepository.findByEan(ean);

        if(productDBO == null) {
            productDBO = new ProductDBO();
        }

        BeanUtils.copyProperties(product, productDBO);
        productRepository.save(productDBO);

        log.info("Product is updated {}", product);
    }

    public Product getByEan(String ean) {
        log.info("Retrieving product by ean {}", ean);
        final ProductDBO productDBO = productRepository.findByEan(ean);

        if (productDBO == null) {
            final String msg = "Product not found: " + ean;
            log.error(msg);
            throw new ProductNotFoundException(msg);
        }
        Product product = new Product();
        BeanUtils.copyProperties(productDBO, product);
        log.info("Product is retrieved {}", product);
        return product;
    }


    public boolean delete(String ean) {
        log.info("The product by ean: {} is about to be deleted!", ean);
        final Long affectedRows = productRepository.deleteByEan(ean);

        if (affectedRows == 0){
            log.info("The product with ean: {} was not in DB.", ean);
            return false;
        }
        log.info("The product by ean: {} is deleted!", ean);
        return true;
    }

    public String getProductCategoryUrl(String ean){
        final ProductDBO productDBO = productRepository.findByEan(ean);

        if (productDBO == null) {
            final String errorMsg = "Product not found: " + ean;
            log.error(errorMsg);
            throw new ProductNotFoundException(errorMsg);
        }

        return categoryService.buildFullUrlPath(productDBO.getCategoryId());
    }

    private void setPriceInfo(PriceInfo priceInfo, ProductDBO productDBO){
        if(priceInfo == null || priceInfo.getPrice() == null){
            throw new InvalidRequestDataException("The price info is missing..");
        }
        final BigDecimal amount = priceService.exchange(priceInfo.getPrice(), priceInfo.getCurrency());
        PriceInfoDBO priceInfoDBO = new PriceInfoDBO(amount);
        productDBO.setPriceInfo(priceInfoDBO);
    }
}
