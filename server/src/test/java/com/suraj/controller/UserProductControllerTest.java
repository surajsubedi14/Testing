package com.suraj.controller;

import com.suraj.exception.ProductException;
import com.suraj.modal.Category;
import com.suraj.modal.Product;
import com.suraj.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private UserProductController userProductController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userProductController).build();
    }

    @Test
    void findProductByCategoryHandler() throws Exception {
        // Mock request parameters
        String category = "electronics";
        List<String> colors = Arrays.asList("red", "blue");
        List<String> sizes = Arrays.asList("M", "L");
        Integer minPrice = 100;
        Integer maxPrice = 1000;
        Integer minDiscount = 10;
        String sort = "price";
        String stock = "available";
        Integer pageNumber = 1;
        Integer pageSize = 10;

        // Create mock response data using the correct method setTitle()
        Product product = new Product();
        product.setTitle("Smartphone");  // Correct method is setTitle(), not setName()
        product.setCategory(new Category("Electronics"));  // Assuming Category is a valid object

        Page<Product> mockPage = new PageImpl<>(Arrays.asList(product));

        // Mock ProductService call
//        when(productService.getAllProduct(category, colors, sizes, minPrice, maxPrice, minDiscount, sort, stock, pageNumber, pageSize))
//                .thenReturn(mockPage);
//
//        // Perform the GET request and verify the response
//        mockMvc.perform(get("/api/products")
//                        .param("category", category)
//                        .param("color", "red,blue")
//                        .param("size", "M,L")
//                        .param("minPrice", minPrice.toString())
//                        .param("maxPrice", maxPrice.toString())
//                        .param("minDiscount", minDiscount.toString())
//                        .param("sort", sort)
//                        .param("stock", stock)
//                        .param("pageNumber", pageNumber.toString())
//                        .param("pageSize", pageSize.toString()))
//                .andExpect(status().isAccepted())
//                .andExpect(jsonPath("$.content[0].title").value("Smartphone"))  // Checking the title
//                .andExpect(jsonPath("$.content[0].category.name").value("Electronics"));  // Assuming category has name property
    }

    @Test
    void findProductByIdHandler() throws Exception {
        // Prepare mock data
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setTitle("Smartphone");

        // Mock ProductService to return the product by ID
//        when(productService.findProductById(productId)).thenReturn(product);
//
//        // Perform the GET request and verify the response
//        mockMvc.perform(get("/api/products/id/{productId}", productId))
//                .andExpect(status().isAccepted())
//                .andExpect(jsonPath("$.name").value("Smartphone"))
//                .andExpect(jsonPath("$.id").value(productId));
    }

    @Test
    void searchProductHandler() throws Exception {
        // Mock request parameter
        String query = "Smartphone";

        // Create mock response data
        Product product = new Product();
        product.setTitle("Smartphone");

        List<Product> products = Arrays.asList(product);

        // Mock ProductService to return the search results
//        when(productService.searchProduct(query)).thenReturn(products);
//
//        // Perform the GET request and verify the response
//        mockMvc.perform(get("/api/products/search")
//                        .param("q", query))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].name").value("Smartphone"));
    }
}
