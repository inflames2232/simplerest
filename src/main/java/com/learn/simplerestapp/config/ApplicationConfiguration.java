package com.learn.simplerestapp.config;

import com.learn.simplerestapp.dao.ProductDAO;
import com.learn.simplerestapp.dto.request.ProductRequest;
import com.learn.simplerestapp.dto.request.TestRequest;
import com.learn.simplerestapp.jooq.domain.tables.records.ProductsRecord;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class ApplicationConfiguration {

    @Autowired
    private ProductDAO productDAO;

    private Converter<Integer, ProductsRecord> idToProductRecordConverter() {
        return context -> {
            Integer source = context.getSource();
            return productDAO.findById(source).orElse(null);
        };
    }
    private PropertyMap<ProductRequest, TestRequest> getPropertyMap() {
        return new PropertyMap<ProductRequest, TestRequest>() {
            @Override
            protected void configure() {
                using(idToProductRecordConverter()).map(source.getRecordId(), destination.getProductsRecord());
            }
        };
    }
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(getPropertyMap());
        return modelMapper;
    }


}
