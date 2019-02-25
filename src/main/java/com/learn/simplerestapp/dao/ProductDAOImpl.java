package com.learn.simplerestapp.dao;

import com.learn.simplerestapp.exceptions.ProductNotFoundException;
import com.learn.simplerestapp.jooq.domain.tables.records.ProductsRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.learn.simplerestapp.jooq.domain.tables.Products.PRODUCTS;

@Repository
@RequiredArgsConstructor
public class ProductDAOImpl implements ProductDAO {

    private final DSLContext dsl;

    @Override
    public ProductsRecord create(ProductsRecord productsRecord) {
        return dsl.insertInto(PRODUCTS)
                .set(productsRecord)
                .returning()
                .fetchOne();
    }

    @Override
    public Result<ProductsRecord> findAll() {
         return dsl.select().from(PRODUCTS).fetch().into(PRODUCTS);
    }

    @Override
    public Optional<ProductsRecord> findById(Integer id) throws ProductNotFoundException {
        return dsl.select().from(PRODUCTS)
                .where(PRODUCTS.PRODUCT_ID.eq(id))
                .fetchOptionalInto(PRODUCTS);
    }

    @Override
    public ProductsRecord updateById(ProductsRecord productsRecord, Integer id) {
        return dsl.update(PRODUCTS)
                .set(PRODUCTS.NAME, productsRecord.getName())
                .set(PRODUCTS.PRICE, productsRecord.getPrice())
                .set(PRODUCTS.DESCRIPTION, productsRecord.getDescription())
                .set(PRODUCTS.STATUS, productsRecord.getStatus())
                .where(PRODUCTS.PRODUCT_ID.eq(id))
                .returning()
                .fetchOne().into(PRODUCTS);
    }

    @Override
    public void delete(ProductsRecord productsRecord) {
        dsl.delete(PRODUCTS)
                .where(PRODUCTS.PRODUCT_ID.eq(productsRecord.getProductId()))
                .execute();
    }
}
