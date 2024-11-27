package com.suraj.controller;

import com.suraj.exception.ProductException;
import com.suraj.exception.UserException;
import com.suraj.modal.Rating;
import com.suraj.modal.User;
import com.suraj.request.RatingRequest;
import com.suraj.service.RatingServices;
import com.suraj.service.UserService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RatingControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private RatingServices ratingServices;

    @InjectMocks
    private RatingController ratingController;

    private User user;
    private RatingRequest ratingRequest;
    private Rating rating;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("testuser@example.com");

        ratingRequest = new RatingRequest();
        ratingRequest.setProductId(1L);
        ratingRequest.setRating(4);
        //ratingRequest.setReview("Good product!");

        rating = new Rating();
        rating.setId(1L);
        rating.setUser(user);
        //rating.setProductId(1L);
        rating.setRating(4);
       // rating.setReview("Good product!");
    }

    @AfterEach
    void tearDown() {
        // Clean up resources if necessary
    }

    @Test
    void createRatingHandler() throws UserException, ProductException {
        // Arrange
        String jwt = "mockJwtToken";
        when(userService.findUserProfileByJwt(jwt)).thenReturn(user);
        when(ratingServices.createRating(ratingRequest, user)).thenReturn(rating);

        // Act
        ResponseEntity<Rating> response = ratingController.createRatingHandler(ratingRequest, jwt);

        // Assert
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(rating.getId(), response.getBody().getId());
        //assertEquals("Good product!", response.getBody().getReview());
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(ratingServices, times(1)).createRating(ratingRequest, user);
    }

    @Test
    void getProductsReviewHandler() {
        // Arrange
        Long productId = 1L;
        List<Rating> ratingsList = Arrays.asList(rating);
        when(ratingServices.getProductsRating(productId)).thenReturn(ratingsList);

        // Act
        ResponseEntity<List<Rating>> response = ratingController.getProductsReviewHandler(productId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        //assertEquals("Good product!", response.getBody().get(0).getReview());
        verify(ratingServices, times(1)).getProductsRating(productId);
    }
}
