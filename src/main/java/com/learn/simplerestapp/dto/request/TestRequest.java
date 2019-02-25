package com.learn.simplerestapp.dto.request;

import com.learn.simplerestapp.jooq.domain.tables.records.ProductsRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestRequest {
    private String name;
    private String description;
    private ProductsRecord productsRecord;
    private Short status;
}
