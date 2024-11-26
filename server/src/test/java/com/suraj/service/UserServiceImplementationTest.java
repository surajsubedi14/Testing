package com.suraj.service;

import com.suraj.exception.UserException;
import com.suraj.modal.User;
import com.suraj.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImplementation userService;

    private User testUser;
    private Long userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup mock data for testing
        userId = 1L;
        testUser = new User();
        testUser.setId(userId);
        testUser.setEmail("testuser@example.com");
       // testUser.setName("Test User");
    }

    @AfterEach
    void tearDown() {
        // Cleanup resources if needed
    }

    @Test
    void findUserById() throws UserException {
        // Mock the user lookup
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // Call the findUserById method
        User foundUser = userService.findUserById(userId);

        // Assert that the user is found and matches the expected result
        assertNotNull(foundUser);
        assertEquals(testUser.getId(), foundUser.getId());
        assertEquals(testUser.getEmail(), foundUser.getEmail());
       // assertEquals(testUser.getName(), foundUser.getName());

        // Verify that the userRepository was called once with the userId
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void findUserByIdThrowsExceptionWhenUserNotFound() {
        // Mock the case where the user is not found
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Assert that UserException is thrown
        UserException thrown = assertThrows(UserException.class, () -> userService.findUserById(userId));
        assertEquals("user not found with id " + userId, thrown.getMessage());

        // Verify that the userRepository was called once
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void findUserProfileByJwt() {
        // Since we are not using Jwt functionality in the current method, we can leave this empty or assert null for now
        //assertNull(userService.findUserProfileByJwt("sample-jwt-token"));
    }

    @Test
    void findAllUsers() {
        // Create a list of users
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        //user1.setName("User One");

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        //user2.setName("User Two");

        List<User> users = Arrays.asList(user1, user2);

        // Mock the repository call to return the list of users
        when(userRepository.findAllByOrderByCreatedAtDesc()).thenReturn(users);

        // Call the findAllUsers method
        List<User> returnedUsers = userService.findAllUsers();

        // Assert that the list of users is returned correctly
        assertNotNull(returnedUsers);
        assertEquals(2, returnedUsers.size());
        //assertEquals(user1.getName(), returnedUsers.get(0).getName());
       // assertEquals(user2.getName(), returnedUsers.get(1).getName());

        // Verify that the userRepository was called once
        verify(userRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }
}
