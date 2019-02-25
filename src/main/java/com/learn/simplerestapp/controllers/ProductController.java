package com.learn.simplerestapp.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.learn.simplerestapp.dto.Views;
import com.learn.simplerestapp.dto.request.ProductRequest;
import com.learn.simplerestapp.dto.request.ProductRequestList;
import com.learn.simplerestapp.dto.request.TestRequest;
import com.learn.simplerestapp.dto.response.ProductResponse;
import com.learn.simplerestapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.Cacheable;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    @JsonView(Views.Public.class)
    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() {
        List<ProductResponse> allProducts = productService.getAllProducts();
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    @PostMapping("/products")
     public ResponseEntity<?> createNewProduct(
            @Validated(ProductRequest.CreateProductRequest.class) @RequestBody ProductRequest productRequest) {
        //TestRequest testRequest = modelMapper.map(productRequest, TestRequest.class);
        //log.info("Test Request - {}", testRequest);
        ProductResponse productResponse = productService.createProduct(productRequest);
        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
    }

    @PostMapping("/products/list")
    public ResponseEntity<?> createNewProducts(
            @Valid @RequestBody ProductRequestList productRequestList) {
        List<ProductResponse> productResponseList = productRequestList.getProductRequestList()
            .stream()
                .map(productService::createProduct)
                .collect(Collectors.toList());
        return new ResponseEntity<>(productResponseList, HttpStatus.CREATED);
    }

    @JsonView(Views.Admin.class)
    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Integer id) {
        ProductResponse productResponse = productService.getProductById(id);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable("id") Integer id,
            @Validated(ProductRequest.UpdateProductRequest.class) @RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.updateOrCreateProduct(productRequest, id);
        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);

    }

    @PatchMapping("/products/{id}")
    public ResponseEntity<?> partialUpdateProduct(
            @PathVariable("id") Integer id,
            @RequestBody Map<String, Object> updates
    ) {
        ProductResponse productResponse = productService.partialUpdateProduct(updates, id);
        
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct( @PathVariable("id") Integer id) {
        productService.deleteProductById(id);
        return new ResponseEntity<>("Record Deleted", HttpStatus.NO_CONTENT);
    }
}
