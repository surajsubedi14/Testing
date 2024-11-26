package com.suraj.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.suraj.exception.UserException;
import com.suraj.modal.User;
import com.suraj.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

class AdminUserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminUserController adminUserController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers() throws UserException {
        // Arrange
        String mockJwt = "Bearer mock-token";
        List<User> mockUsers = Arrays.asList(new User(), new User());
        when(userService.findAllUsers()).thenReturn(mockUsers);

        // Act
        ResponseEntity<List<User>> response = adminUserController.getAllUsers(mockJwt);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(mockUsers, response.getBody());
        verify(userService, times(1)).findAllUsers();
    }
}
