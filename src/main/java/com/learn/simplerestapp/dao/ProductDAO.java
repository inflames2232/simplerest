package com.learn.simplerestapp.dao;

import com.learn.simplerestapp.jooq.domain.tables.records.ProductsRecord;

import java.util.List;
import java.util.Optional;

public interface ProductDAO {
    ProductsRecord create(ProductsRecord productsRecord);
    List<ProductsRecord> findAll();
    Optional<ProductsRecord> findById(Integer id);
    ProductsRecord updateById(ProductsRecord productsRecord, Integer id);
    void delete(ProductsRecord productsRecord);
}
