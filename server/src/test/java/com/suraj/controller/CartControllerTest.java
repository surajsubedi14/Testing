package com.suraj.controller;

import com.suraj.exception.ProductException;
import com.suraj.exception.UserException;
import com.suraj.modal.Cart;
import com.suraj.modal.CartItem;
import com.suraj.modal.User;
import com.suraj.request.AddItemRequest;
import com.suraj.service.CartService;
import com.suraj.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CartController cartController;

    private User mockUser;
    private Cart mockCart;
    private CartItem mockCartItem;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");

        mockCart = new Cart();
        mockCart.setUser(mockUser);

        mockCartItem = new CartItem();
        mockCartItem.setId(1L);
//        mockCartItem.setProductName("Test Product");
        mockCartItem.setQuantity(2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findUserCartHandler() throws Exception {
        // Mock userService to return the mock user
        when(userService.findUserProfileByJwt(anyString())).thenReturn(mockUser);

        // Mock cartService to return the mock cart
        when(cartService.findUserCart(mockUser.getId())).thenReturn(mockCart);

        // Perform the request
        mockMvc.perform(get("/api/cart/")
                        .header("Authorization", "Bearer mock-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email").value("test@example.com"));

        verify(cartService, times(1)).findUserCart(mockUser.getId());
    }

    @Test
    void addItemToCart() throws Exception {
        // Mock userService to return the mock user
        when(userService.findUserProfileByJwt(anyString())).thenReturn(mockUser);

        // Mock cartService to add the mock item to the cart
        when(cartService.addCartItem(mockUser.getId(), any(AddItemRequest.class))).thenReturn(mockCartItem);

        // Create a request body
        AddItemRequest addItemRequest = new AddItemRequest();
        addItemRequest.setProductId(1L);
        addItemRequest.setQuantity(2);

        // Perform the request
        mockMvc.perform(put("/api/cart/add")
                        .header("Authorization", "Bearer mock-jwt-token")
                        .contentType("application/json")
                        .content("{\"productId\":1, \"quantity\":2}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productName").value("Test Product"))
                .andExpect(jsonPath("$.quantity").value(2));

        verify(cartService, times(1)).addCartItem(mockUser.getId(), addItemRequest);
    }

    @Test
    void addItemToCart_UserNotFound() throws Exception {
        // Mock userService to throw UserException
        when(userService.findUserProfileByJwt(anyString())).thenThrow(new UserException("User not found"));

        // Perform the request
        mockMvc.perform(put("/api/cart/add")
                        .header("Authorization", "Bearer mock-jwt-token")
                        .contentType("application/json")
                        .content("{\"productId\":1, \"quantity\":2}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User not found"));

        verify(cartService, times(0)).addCartItem(anyLong(), any(AddItemRequest.class));
    }

    @Test
    void addItemToCart_ProductNotFound() throws Exception {
        // Mock userService to return the mock user
        when(userService.findUserProfileByJwt(anyString())).thenReturn(mockUser);

        // Mock cartService to throw ProductException
        when(cartService.addCartItem(mockUser.getId(), any(AddItemRequest.class))).thenThrow(new ProductException("Product not found"));

        // Create a request body
        AddItemRequest addItemRequest = new AddItemRequest();
        addItemRequest.setProductId(1L);
        addItemRequest.setQuantity(2);

        // Perform the request
        mockMvc.perform(put("/api/cart/add")
                        .header("Authorization", "Bearer mock-jwt-token")
                        .contentType("application/json")
                        .content("{\"productId\":1, \"quantity\":2}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Product not found"));

        verify(cartService, times(1)).addCartItem(mockUser.getId(), addItemRequest);
    }
}
