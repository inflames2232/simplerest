package com.learn.simplerestapp;

import com.learn.simplerestapp.controllers.ProductController;
import com.learn.simplerestapp.dto.request.ProductRequest;
import com.learn.simplerestapp.dto.response.ProductResponse;
import com.learn.simplerestapp.service.ProductService;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private JacksonTester<ProductResponse> json;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productServiceMock;

    @Before
    public void setUp() {

    }

    @Test
    public void shouldAddProduct() throws Exception {
        Timestamp timestampNow = Timestamp.valueOf(LocalDateTime.now());
        ProductResponse productResponse = new ProductResponse(1, "Chair", "Cool Chair",
                new BigDecimal(100), (short) 1, timestampNow, timestampNow);
        when(productServiceMock.createProduct(any(ProductRequest.class)))
                .thenReturn(productResponse);

        System.out.println(timestampNow);
        mockMvc.perform(post("/v1/products")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{ \"name\": \"Chair \", \"description\": \"Cool Chair\", \"price\": 202, \"status\": 1 }"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.name").value("Chair"))
                .andExpect(jsonPath("$.description").value("Cool Chair"))
                .andExpect(jsonPath("$.price").value(100))
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.createdAt").value(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestampNow)))
                .andExpect(jsonPath("$.updatedAt").value(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestampNow)));
    }

    @Test
    public void shouldGetProductList() throws Exception {
        Timestamp timestampNow = Timestamp.valueOf(LocalDateTime.now());
        ProductResponse productResponse = new ProductResponse(1, "Chair", "Cool Chair",
                new BigDecimal(100), (short) 1, timestampNow, timestampNow);
        ProductResponse productResponse2 = new ProductResponse(2, "Chair2", "Cool Chair2",
                new BigDecimal(200), (short) 0, timestampNow, timestampNow);
        ProductResponse productResponse3 = new ProductResponse(3, "Chair3", "Cool Chair3",
                new BigDecimal(300), (short) 1, timestampNow, timestampNow);

        when(productServiceMock.getAllProducts()).thenReturn(Lists.list(productResponse, productResponse2, productResponse3));

        mockMvc.perform(get("/v1/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].productId").value(containsInAnyOrder(1,2,3)))
                .andExpect(jsonPath("$[*].name").value(containsInAnyOrder("Chair","Chair2","Chair3")))
                .andExpect(jsonPath("$[0].status").value(1))
                .andExpect(jsonPath("$[*].description").value(containsInAnyOrder("Cool Chair3", "Cool Chair2", "Cool Chair")));
    }

    @Test
    public void shouldPartialEdit() throws Exception {
        Timestamp timestampNow = Timestamp.valueOf(LocalDateTime.now());
        Integer id = 1;
        ProductResponse productResponse = new ProductResponse(id, "Chair", "Cool Chair",
                new BigDecimal(100), (short) 1, timestampNow, timestampNow);

        when(productServiceMock.partialUpdateProduct(anyMap(), eq(id))).thenReturn(productResponse);

        mockMvc.perform(patch("/v1/products/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"Chair \", \"description\": \"Cool Chair\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.productId").value(id))
                .andExpect(jsonPath("$.name").value("Chair"))
                .andExpect(jsonPath("$.description").value("Cool Chair"))
                .andExpect(jsonPath("$.price").value(100))
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.createdAt").value(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestampNow)))
                .andExpect(jsonPath("$.updatedAt").value(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestampNow)));
    }
}
