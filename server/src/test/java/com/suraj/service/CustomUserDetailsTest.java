package com.suraj.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.suraj.modal.User;
import com.suraj.repository.UserRepository;

class CustomUserDetailsTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        // Arrange
        String email = "rushi.kothari@iiitb.ac.in";
        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword("password123");

        when(userRepository.findByEmail(email)).thenReturn(mockUser);

        // Act
        UserDetails userDetails = customUserDetails.loadUserByUsername(email);

        // Assert
        assertNotNull(userDetails, "UserDetails should not be null");
        assertEquals(email, userDetails.getUsername(), "Email should match the input username");
        assertEquals(mockUser.getPassword(), userDetails.getPassword(), "Password should match the user's password");
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void loadUserByUsername_UserDoesNotExist_ThrowsException() {
        // Arrange
        String email = "suraj.subedi@iiitb.ac.in";

        when(userRepository.findByEmail(email)).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetails.loadUserByUsername(email);
        });

        assertEquals("user not found with email " + email, exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }
}
