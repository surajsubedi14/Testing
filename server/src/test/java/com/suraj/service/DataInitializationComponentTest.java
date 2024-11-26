package com.suraj.service;

import static org.mockito.Mockito.*;

import com.suraj.modal.User;
import com.suraj.repository.UserRepository;
import com.suraj.user.domain.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

class DataInitializationComponentTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CartService cartService;

    @InjectMocks
    private DataInitializationComponent dataInitializationComponent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void run_AdminUserAlreadyExists_DoesNotCreateAdmin() {
        // Arrange
        String adminEmail = "codewithzosh@gmail.com";
        when(userRepository.findByEmail(adminEmail)).thenReturn(new User());

        // Act
        dataInitializationComponent.run();

        // Assert
        verify(userRepository, times(1)).findByEmail(adminEmail);
        verify(userRepository, never()).save(any(User.class));
        verify(cartService, never()).createCart(any(User.class));
    }

    @Test
    void run_AdminUserDoesNotExist_CreatesAdmin() {
        // Arrange
        String adminEmail = "codewithzosh@gmail.com";
        User savedUser = new User();
        savedUser.setEmail(adminEmail);

        when(userRepository.findByEmail(adminEmail)).thenReturn(null);
        when(passwordEncoder.encode("codewithzosh")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        dataInitializationComponent.run();

        // Assert
        verify(userRepository, times(1)).findByEmail(adminEmail);
        verify(passwordEncoder, times(1)).encode("codewithzosh");
        verify(userRepository, times(1)).save(any(User.class));
        verify(cartService, times(1)).createCart(savedUser);
    }
}
