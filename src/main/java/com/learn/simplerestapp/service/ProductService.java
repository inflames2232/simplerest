package com.learn.simplerestapp.service;


import com.learn.simplerestapp.dto.request.ProductRequest;
import com.learn.simplerestapp.dto.request.ProductRequestList;
import com.learn.simplerestapp.dto.response.ProductResponse;

import java.util.List;
import java.util.Map;

public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest);
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(Integer id);
    ProductResponse updateOrCreateProduct(ProductRequest productRequest, Integer id);
    ProductResponse partialUpdateProduct(Map<String, Object> updates, Integer id);
    void deleteProductById(Integer id);

}
