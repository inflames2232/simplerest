package com.learn.simplerestapp.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.learn.simplerestapp.dto.Views;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    @JsonView(Views.Admin.class)
    private Integer productId;

    @JsonView(Views.Public.class)
    private String name;

    @JsonView(Views.Full.class)
    private String description;

    @JsonView(Views.Public.class)
    private BigDecimal price;

    @JsonView(Views.Public.class)
    private Short status;

    @JsonView(Views.Public.class)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "Europe/Moscow")
    private Timestamp createdAt;

    @JsonView(Views.Public.class)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "Europe/Moscow")
    private Timestamp updatedAt;
}
