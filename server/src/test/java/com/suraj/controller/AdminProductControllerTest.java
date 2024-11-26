package com.suraj.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.suraj.exception.ProductException;
import com.suraj.modal.Product;
import com.suraj.request.CreateProductRequest;
import com.suraj.response.ApiResponse;
import com.suraj.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

class AdminProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private AdminProductController adminProductController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProductHandler() throws ProductException {
        CreateProductRequest req = new CreateProductRequest();
        Product mockProduct = new Product();
        when(productService.createProduct(req)).thenReturn(mockProduct);

        ResponseEntity<Product> response = adminProductController.createProductHandler(req);

        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(mockProduct, response.getBody());
        verify(productService, times(1)).createProduct(req);
    }

    @Test
    void deleteProductHandler() throws ProductException {
        Long productId = 1L;
        String mockMessage = "Product deleted successfully";
        when(productService.deleteProduct(productId)).thenReturn(mockMessage);

        ResponseEntity<ApiResponse> response = adminProductController.deleteProductHandler(productId);

        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(mockMessage, response.getBody().getMessage());
//        assertTrue(response.getBody().isSuccess());
        verify(productService, times(1)).deleteProduct(productId);
    }

    @Test
    void findAllProduct() {
        List<Product> mockProducts = Arrays.asList(new Product(), new Product());
        when(productService.getAllProducts()).thenReturn(mockProducts);

        ResponseEntity<List<Product>> response = adminProductController.findAllProduct();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockProducts, response.getBody());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void recentlyAddedProduct() {
        List<Product> mockProducts = Arrays.asList(new Product(), new Product());
        when(productService.recentlyAddedProduct()).thenReturn(mockProducts);

        ResponseEntity<List<Product>> response = adminProductController.recentlyAddedProduct();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockProducts, response.getBody());
        verify(productService, times(1)).recentlyAddedProduct();
    }

    @Test
    void updateProductHandler() throws ProductException {
        Long productId = 1L;
        Product req = new Product();
        Product mockUpdatedProduct = new Product();
        when(productService.updateProduct(productId, req)).thenReturn(mockUpdatedProduct);

        ResponseEntity<Product> response = adminProductController.updateProductHandler(req, productId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUpdatedProduct, response.getBody());
        verify(productService, times(1)).updateProduct(productId, req);
    }

    @Test
    void createMultipleProduct() throws ProductException {
        // Arrange
        CreateProductRequest[] productRequests = {
                new CreateProductRequest(), new CreateProductRequest()
        };

        // Act
        ApiResponse response = new ApiResponse("products created successfully", true);
        when(productService.createProduct(any(CreateProductRequest.class))).thenReturn(null);  // Mock createProduct to return null

        ResponseEntity<ApiResponse> result = adminProductController.createMultipleProduct(productRequests);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
        assertEquals("products created successfully", result.getBody().getMessage());
        verify(productService, times(2)).createProduct(any(CreateProductRequest.class));  // Ensure createProduct was called twice
    }
}
