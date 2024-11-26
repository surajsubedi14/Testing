package com.suraj.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.suraj.exception.ProductException;
import com.suraj.modal.Category;
import com.suraj.modal.Product;
import com.suraj.repository.CategoryRepository;
import com.suraj.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplementationTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserService userService;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImplementation productService;

    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setName("Category1");

        product = new Product();
        product.setId(1L);
        product.setTitle("Product1");
        product.setColor("Red");
        product.setDescription("Description1");
        product.setDiscountedPrice(80);
        product.setDiscountPersent(20);
        product.setImageUrl("imageUrl");
        product.setBrand("Brand1");
        product.setPrice(100);
//        product.setSizes(Arrays.asList("M", "L"));
        product.setQuantity(10);
        product.setCategory(category);
        product.setCreatedAt(LocalDateTime.now());
    }


    @Test
    void testDeleteProduct() throws ProductException {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        String result = productService.deleteProduct(1L);

        assertEquals("Product deleted Successfully", result);
        verify(productRepository, times(1)).delete(any(Product.class));
    }

    @Test
    void testUpdateProduct() throws ProductException {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = new Product();
        updatedProduct.setQuantity(20);
        updatedProduct.setDescription("New Description");

        Product result = productService.updateProduct(1L, updatedProduct);

        assertNotNull(result);
        assertEquals(20, result.getQuantity());
        assertEquals("New Description", result.getDescription());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testGetAllProducts() {
        List<Product> productList = Arrays.asList(product);
        when(productRepository.findAll()).thenReturn(productList);

        List<Product> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Product1", result.get(0).getTitle());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindProductById() throws ProductException {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        Product result = productService.findProductById(1L);

        assertNotNull(result);
        assertEquals("Product1", result.getTitle());
        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test
    void testFindProductByCategory() {
        List<Product> productList = Arrays.asList(product);
        when(productRepository.findByCategory(anyString())).thenReturn(productList);

        List<Product> result = productService.findProductByCategory("Category1");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Product1", result.get(0).getTitle());
        verify(productRepository, times(1)).findByCategory(anyString());
    }

    @Test
    void testSearchProduct() {
        List<Product> productList = Arrays.asList(product);
        when(productRepository.searchProduct(anyString())).thenReturn(productList);

        List<Product> result = productService.searchProduct("Product");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Product1", result.get(0).getTitle());
        verify(productRepository, times(1)).searchProduct(anyString());
    }

    @Test
    void testGetAllProduct() {
        List<Product> productList = Arrays.asList(product);
        when(productRepository.filterProducts(anyString(), anyInt(), anyInt(), anyInt(), anyString())).thenReturn(productList);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());

        Page<Product> result = productService.getAllProduct("Category1", Arrays.asList("Red"), Arrays.asList("M"), 0, 200, 10, "asc", "in_stock", 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Product1", result.getContent().get(0).getTitle());
        verify(productRepository, times(1)).filterProducts(anyString(), anyInt(), anyInt(), anyInt(), anyString());
    }

    @Test
    void testRecentlyAddedProduct() {
        List<Product> productList = Arrays.asList(product);
        when(productRepository.findTop10ByOrderByCreatedAtDesc()).thenReturn(productList);

        List<Product> result = productService.recentlyAddedProduct();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Product1", result.get(0).getTitle());
        verify(productRepository, times(1)).findTop10ByOrderByCreatedAtDesc();
    }
}
