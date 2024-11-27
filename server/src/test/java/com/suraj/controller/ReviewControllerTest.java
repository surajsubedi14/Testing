package com.suraj.controller;

import com.suraj.exception.ProductException;
import com.suraj.exception.UserException;
import com.suraj.modal.Review;
import com.suraj.modal.User;
import com.suraj.request.ReviewRequest;
import com.suraj.service.ReviewService;
import com.suraj.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ReviewController reviewController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createReviewHandler() throws Exception {
        // Prepare mock data
        Long productId = 1L;
        String reviewText = "Great product!";
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setProductId(productId);
        reviewRequest.setReview(reviewText);

        User mockUser = new User();
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        // Assume UserService mock returns this mockUser when called
        when(userService.findUserProfileByJwt(anyString())).thenReturn(mockUser);

        Review mockReview = new Review();
        //mockReview.setProductId(productId);
        mockReview.setReview(reviewText);
        // Assume ReviewService mock returns this mockReview when called
        when(reviewService.createReview(any(), eq(mockUser))).thenReturn(mockReview);

        // Perform the POST request
        mockMvc.perform(post("/api/reviews/create")
                        .contentType("application/json")
                        .header("Authorization", "Bearer some-jwt-token")
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isAccepted())  // Expect HTTP 202 status
                .andExpect(jsonPath("$.productId").value(productId)) // Verify productId in response
                .andExpect(jsonPath("$.review").value(reviewText)); // Verify review in response
    }

    @Test
    void getProductsReviewHandler() throws Exception {
        // Prepare mock data
        Long productId = 1L;
        Review mockReview1 = new Review();
        //mockReview1.setProductId(productId);
        mockReview1.setReview("Amazing product!");

        Review mockReview2 = new Review();
        //mockReview2.setProductId(productId);
        mockReview2.setReview("Good quality!");

        // Assume ReviewService mock returns a list of reviews
        //when(reviewService.getAllReview(productId)).thenReturn(List.of(mockReview1, mockReview2));

        // Perform the GET request
        mockMvc.perform(get("/api/reviews/product/{productId}", productId))
                .andExpect(status().isOk()) // Expect HTTP 200 status
                .andExpect(jsonPath("$[0].productId").value(productId)) // Verify productId in first review
                .andExpect(jsonPath("$[0].review").value("Amazing product!")) // Verify review in first review
                .andExpect(jsonPath("$[1].review").value("Good quality!")); // Verify review in second review
    }
}
