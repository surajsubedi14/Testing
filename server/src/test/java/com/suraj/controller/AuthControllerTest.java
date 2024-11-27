package com.suraj.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.suraj.exception.UserException;
import com.suraj.modal.User;
import com.suraj.repository.UserRepository;
import com.suraj.request.LoginRequest;
import com.suraj.response.AuthResponse;
import com.suraj.service.CartService;
import com.suraj.service.CustomUserDetails;
import com.suraj.config.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private CustomUserDetails customUserDetails;

    @Mock
    private CartService cartService;

    @InjectMocks
    private AuthController authController;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password");
        mockUser.setFirstName("Test");
        mockUser.setLastName("User");
        mockUser.setRole("USER");
    }

    @Test
    void createUserHandler() throws UserException {
        // Arrange
        User newUser = new User();
        newUser.setEmail("test@example.com");
        newUser.setPassword("password");
        newUser.setFirstName("Test");
        newUser.setLastName("User");
        newUser.setRole("USER");

        when(userRepository.findByEmail("test@example.com")).thenReturn(null);  // Simulate no existing user
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn("mocked-token");

        // Act
        ResponseEntity<AuthResponse> response = authController.createUserHandler(newUser);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("mocked-token", response.getBody().getJwt());
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(userRepository, times(1)).save(any(User.class));
        verify(cartService, times(1)).createCart(mockUser);
    }

    @Test
    void createUserHandler_EmailAlreadyExists() throws UserException {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);  // Simulate existing user

        // Act & Assert
        UserException exception = assertThrows(UserException.class, () -> {
            authController.createUserHandler(mockUser);
        });
        assertEquals("Email Is Already Used With Another Account", exception.getMessage());
    }

    @Test
    void signin() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword(passwordEncoder.encode("password"));

//        when(customUserDetails.loadUserByUsername("test@example.com")).thenReturn(mockUser);
        when(passwordEncoder.matches("password", mockUser.getPassword())).thenReturn(true);
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn("mocked-token");

        // Act
        ResponseEntity<AuthResponse> response = authController.signin(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("mocked-token", response.getBody().getJwt());
    }


    void signin_InvalidCredentials() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongpassword");

//         when(customUserDetails.loadUserByUsername("test@example.com")).thenReturn(mockUser);
        when(passwordEncoder.matches("wrongpassword", mockUser.getPassword())).thenReturn(false);

        // Act & Assert
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            authController.signin(loginRequest);
        });
        assertEquals("Invalid username or password", exception.getMessage());
    }
}
