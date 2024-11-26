package com.suraj.service;

import com.suraj.exception.ProductException;
import com.suraj.modal.Product;
import com.suraj.modal.Review;
import com.suraj.modal.User;
import com.suraj.repository.ProductRepository;
import com.suraj.repository.ReviewRepository;
import com.suraj.request.ReviewRequest;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ReviewServiceImplementationTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ReviewServiceImplementation reviewService;

    private Product testProduct;
    private User testUser;
    private ReviewRequest testReviewRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup mock data for testing
        testUser = new User();
        testUser.setId(1L);

        testProduct = new Product();
        testProduct.setId(1L);
//        testProduct.setName("Test Product");

        testReviewRequest = new ReviewRequest();
        testReviewRequest.setProductId(1L);
        testReviewRequest.setReview("Great product!");
    }

    @AfterEach
    void tearDown() {
        // You can use this to clear resources or mock states if needed
    }

    @Test
    void createReview() throws ProductException {
        // Mock the product lookup and review creation
        when(productService.findProductById(testReviewRequest.getProductId())).thenReturn(testProduct);

        Review savedReview = new Review();
        savedReview.setUser(testUser);
        savedReview.setProduct(testProduct);
        savedReview.setReview(testReviewRequest.getReview());
        savedReview.setCreatedAt(LocalDateTime.now());

        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);

        // Call the createReview method
        Review createdReview = reviewService.createReview(testReviewRequest, testUser);

        // Verify the result
        assertNotNull(createdReview);
        assertEquals(testUser, createdReview.getUser());
        assertEquals(testProduct, createdReview.getProduct());
        assertEquals(testReviewRequest.getReview(), createdReview.getReview());
        assertNotNull(createdReview.getCreatedAt());

        // Verify interactions
        verify(productService, times(1)).findProductById(testReviewRequest.getProductId());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void createReviewThrowsExceptionWhenProductNotFound() throws ProductException {
        // Mock the scenario where the product is not found
        when(productService.findProductById(testReviewRequest.getProductId())).thenThrow(new ProductException("Product not found"));

        // Assert that the ProductException is thrown
        ProductException thrown = assertThrows(ProductException.class, () -> reviewService.createReview(testReviewRequest, testUser));
        assertEquals("Product not found", thrown.getMessage());

        // Verify that the product lookup was called
        verify(productService, times(1)).findProductById(testReviewRequest.getProductId());
    }

    @Test
    void getAllReview() {
        // Mock some reviews for the product
        Review review1 = new Review();
        review1.setUser(testUser);
        review1.setProduct(testProduct);
        review1.setReview("Great product!");
        review1.setCreatedAt(LocalDateTime.now());

        Review review2 = new Review();
        review2.setUser(testUser);
        review2.setProduct(testProduct);
        review2.setReview("Good value for money.");
        review2.setCreatedAt(LocalDateTime.now());

        List<Review> reviews = new ArrayList<>();
        reviews.add(review1);
        reviews.add(review2);

        // Mock the repository call to get all reviews
        when(reviewRepository.getAllProductsReview(testProduct.getId())).thenReturn(reviews);

        // Call the getAllReview method
        List<Review> returnedReviews = reviewService.getAllReview(testProduct.getId());

        // Assert that the reviews are returned correctly
        assertNotNull(returnedReviews);
        assertEquals(2, returnedReviews.size());
        assertEquals("Great product!", returnedReviews.get(0).getReview());
        assertEquals("Good value for money.", returnedReviews.get(1).getReview());

        // Verify that the review repository was called
        verify(reviewRepository, times(1)).getAllProductsReview(testProduct.getId());
    }
}
