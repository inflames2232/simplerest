package com.learn.simplerestapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    public interface Default {
    }
    public interface CreateProductRequest extends Default {
    }
    public interface UpdateProductRequest extends Default {
    }

   //@NotNull(groups = {UpdateProductRequest.class})
   private Integer recordId;

    @NotNull(groups = {Default.class})
    @NotEmpty(groups = {Default.class})
    @NotBlank(groups = {Default.class})
    private String name;

    @NotNull(groups = {CreateProductRequest.class})
    private String description;

    @NotNull(groups = {Default.class})
    @Digits(integer = 10, fraction = 2, message = "price must be number value", groups = Default.class)
    private BigDecimal price;

    @NotNull(groups = {Default.class})
    private Short status;
}
