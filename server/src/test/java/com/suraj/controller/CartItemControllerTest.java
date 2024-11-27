package com.suraj.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.suraj.exception.CartItemException;
import com.suraj.exception.UserException;
import com.suraj.modal.CartItem;
import com.suraj.modal.User;
import com.suraj.response.ApiResponse;
import com.suraj.service.CartItemService;
import com.suraj.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class CartItemControllerTest {

    @InjectMocks
    private CartItemController cartItemController;

    @Mock
    private CartItemService cartItemService;

    @Mock
    private UserService userService;

    private String mockJwt;
    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockJwt = "mock-jwt-token";
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
    }

    @Test
    void deleteCartItemHandler() throws UserException, CartItemException {
        Long cartItemId = 123L;

        // Mock behavior
        when(userService.findUserProfileByJwt(mockJwt)).thenReturn(mockUser);
        doNothing().when(cartItemService).removeCartItem(mockUser.getId(), cartItemId);

        // Call the method
        ResponseEntity<ApiResponse> response = cartItemController.deleteCartItemHandler(cartItemId, mockJwt);

        // Verify interactions
        verify(userService, times(1)).findUserProfileByJwt(mockJwt);
        verify(cartItemService, times(1)).removeCartItem(mockUser.getId(), cartItemId);

        // Assert response
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
//        assertTrue(response.getBody().getStatus());
        assertEquals("Item Remove From Cart", response.getBody().getMessage());
    }

    @Test
    void updateCartItemHandler() throws UserException, CartItemException {
        Long cartItemId = 123L;
        CartItem mockCartItem = new CartItem();
        mockCartItem.setId(cartItemId);
        mockCartItem.setQuantity(2);

        CartItem updatedCartItem = new CartItem();
        updatedCartItem.setId(cartItemId);
        updatedCartItem.setQuantity(3);

        // Mock behavior
        when(userService.findUserProfileByJwt(mockJwt)).thenReturn(mockUser);
        when(cartItemService.updateCartItem(mockUser.getId(), cartItemId, mockCartItem)).thenReturn(updatedCartItem);

        // Call the method
        ResponseEntity<CartItem> response = cartItemController.updateCartItemHandler(cartItemId, mockCartItem, mockJwt);

        // Verify interactions
        verify(userService, times(1)).findUserProfileByJwt(mockJwt);
        verify(cartItemService, times(1)).updateCartItem(mockUser.getId(), cartItemId, mockCartItem);

        // Assert response
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(updatedCartItem, response.getBody());
    }
}
