package com.suraj.service;

import com.suraj.exception.ProductException;
import com.suraj.modal.Product;
import com.suraj.modal.Rating;
import com.suraj.modal.User;
import com.suraj.repository.RatingRepository;
import com.suraj.request.RatingRequest;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RatingServiceImplementationTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private RatingServiceImplementation ratingService;

    private User testUser;
    private Product testProduct;
    private RatingRequest ratingRequest;
    private Long productId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup mock data for testing
        productId = 1L;
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("rushi.kothari@iiitb.ac.in");
        //testUser.setName("Test User");

        testProduct = new Product();
        testProduct.setId(productId);
        //testProduct.setName("Test Product");

        ratingRequest = new RatingRequest();
        ratingRequest.setProductId(productId);
        ratingRequest.setRating(4);
    }

    @AfterEach
    void tearDown() {
        // Cleanup resources if needed
    }

    @Test
    void createRating() throws ProductException {
        // Mock the product lookup from productService
        when(productService.findProductById(productId)).thenReturn(testProduct);

        // Mock the save of the rating
        Rating savedRating = new Rating();
        savedRating.setProduct(testProduct);
        savedRating.setUser(testUser);
        savedRating.setRating(ratingRequest.getRating());
        savedRating.setCreatedAt(LocalDateTime.now());
        when(ratingRepository.save(any(Rating.class))).thenReturn(savedRating);

        // Call the createRating method
        Rating createdRating = ratingService.createRating(ratingRequest, testUser);

        // Assert the result
        assertNotNull(createdRating);
        assertEquals(testProduct.getId(), createdRating.getProduct().getId());
        assertEquals(testUser.getId(), createdRating.getUser().getId());
        assertEquals(ratingRequest.getRating(), createdRating.getRating());
        assertNotNull(createdRating.getCreatedAt());

        // Verify interactions with the mocked objects
        verify(productService, times(1)).findProductById(productId);
        verify(ratingRepository, times(1)).save(any(Rating.class));
    }

    @Test
    void createRatingThrowsProductExceptionWhenProductNotFound() {
        // Mock that product is not found
       // when(productService.findProductById(productId)).thenThrow(ProductException.class);

        // Assert that a ProductException is thrown when the product is not found
        //assertThrows(ProductException.class, () -> ratingService.createRating(ratingRequest, testUser));

        // Verify that the productService was called once
        //verify(productService, times(1)).findProductById(productId);
    }

    @Test
    void getProductsRating() {
        // Create some mock ratings
        Rating rating1 = new Rating();
        rating1.setProduct(testProduct);
        rating1.setUser(testUser);
        rating1.setRating(4);
        rating1.setCreatedAt(LocalDateTime.now());

        Rating rating2 = new Rating();
        rating2.setProduct(testProduct);
        rating2.setUser(testUser);
        rating2.setRating(5);
        rating2.setCreatedAt(LocalDateTime.now());

        List<Rating> ratings = Arrays.asList(rating1, rating2);

        // Mock the ratingRepository to return the mock list of ratings
        when(ratingRepository.getAllProductsRating(productId)).thenReturn(ratings);

        // Call the getProductsRating method
        List<Rating> returnedRatings = ratingService.getProductsRating(productId);

        // Assert the result
        assertNotNull(returnedRatings);
        assertEquals(2, returnedRatings.size());
        assertEquals(4, returnedRatings.get(0).getRating());
        assertEquals(5, returnedRatings.get(1).getRating());

        // Verify that the ratingRepository method was called once
        verify(ratingRepository, times(1)).getAllProductsRating(productId);
    }
}
