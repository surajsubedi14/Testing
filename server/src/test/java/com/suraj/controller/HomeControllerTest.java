package com.suraj.controller;

import com.suraj.response.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void homeController() {
        // Call the method
        ResponseEntity<ApiResponse> response = homeController.homeController();

        // Assert the response
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
//        assertTrue(response.getBody().getStatus());
        assertEquals("Welcome To E-Commerce System", response.getBody().getMessage());
    }
}
