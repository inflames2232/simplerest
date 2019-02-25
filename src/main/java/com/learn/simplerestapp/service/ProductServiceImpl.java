package com.learn.simplerestapp.service;

import com.learn.simplerestapp.dao.ProductDAO;
import com.learn.simplerestapp.dto.request.ProductRequest;
import com.learn.simplerestapp.dto.response.ProductResponse;
import com.learn.simplerestapp.exceptions.ProductNotFoundException;
import com.learn.simplerestapp.jooq.domain.tables.records.ProductsRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Result;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductDAO productDAO;

    private ProductsRecord createRecord(ProductRequest productRequest) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        ProductsRecord record = new ProductsRecord();
        record.from(productRequest);
        record.setCreatedAt(currentTime);
        record.setUpdatedAt(currentTime);
        return record;
    }

    @CacheEvict(value="products", allEntries=true)
    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        ProductsRecord createRecord = createRecord(productRequest);
        log.info("Record - {} created", createRecord);
        return productDAO.create(createRecord).into(ProductResponse.class);
    }

    @Cacheable("products")
    @Override
    public List<ProductResponse> getAllProducts() {
        return ((Result<ProductsRecord>)productDAO.findAll()).into(ProductResponse.class);
    }

    @Override
    public ProductResponse getProductById(Integer id) throws ProductNotFoundException{
        return productDAO.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found"))
                .into(ProductResponse.class);
    }

    @Override
    @Transactional
    public ProductResponse partialUpdateProduct(Map<String, Object> updates, Integer id) {
        ProductsRecord productsRecord = productDAO.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));
        productsRecord.fromMap(updates);
        return productDAO.updateById(productsRecord, id).into(ProductResponse.class);
    }

    @Override
    public void deleteProductById(Integer id) {
        ProductsRecord productsRecord = productDAO.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));
        productDAO.delete(productsRecord);
    }

    @Override
    @CacheEvict(value="products", allEntries=true)
    @Transactional
    public ProductResponse updateOrCreateProduct(ProductRequest productRequest, Integer id) {
        Optional<ProductsRecord> findRecord = productDAO.findById(id);
        ProductsRecord createRecord = createRecord(productRequest);
        if(findRecord.isPresent()) {
            log.info("Record - {} updated", createRecord);
            return productDAO.updateById(createRecord, id).into(ProductResponse.class);
        } else {
            log.info("Record - {} created", createRecord);
            return productDAO.create(createRecord).into(ProductResponse.class);
        }
    }


}
