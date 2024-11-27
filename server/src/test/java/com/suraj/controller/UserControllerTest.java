package com.suraj.controller;

import com.suraj.exception.UserException;
import com.suraj.modal.User;
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
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getUserProfileHandler() throws Exception {
        // Prepare mock data
        String jwt = "Bearer some-jwt-token";
        User mockUser = new User();
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setEmail("john.doe@example.com");

        // Mock the UserService to return the mock user when findUserProfileByJwt is called
        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);

        // Perform the GET request
        mockMvc.perform(get("/api/users/profile")
                        .header("Authorization", jwt))
                .andExpect(status().isAccepted())  // Expect HTTP 202 status
                .andExpect(jsonPath("$.firstName").value("John"))  // Validate first name
                .andExpect(jsonPath("$.lastName").value("Doe"))  // Validate last name
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));  // Validate email
    }

    // Additional test for handling UserException
    @Test
    void getUserProfileHandler_ShouldThrowUserException() throws Exception {
        // Prepare mock data
        String jwt = "Bearer invalid-jwt-token";

        // Mock the UserService to throw UserException when findUserProfileByJwt is called with an invalid JWT
//        when(userService.findUserProfileByJwt(jwt)).thenThrow(UserException.class);

        // Perform the GET request and expect a 404 Not Found status when UserException is thrown
//        mockMvc.perform(get("/api/users/profile")
//                        .header("Authorization", jwt))
//                .andExpect(status().isNotFound());  // Expect HTTP 404 status for UserException
    }
}
