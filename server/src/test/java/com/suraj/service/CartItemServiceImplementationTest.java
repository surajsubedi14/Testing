package com.suraj.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.suraj.exception.CartItemException;
import com.suraj.exception.UserException;
import com.suraj.modal.CartItem;
import com.suraj.modal.Product;
import com.suraj.modal.User;
import com.suraj.repository.CartItemRepository;
import com.suraj.repository.CartRepository;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceImplementationTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private UserService userService;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartItemServiceImplementation cartItemService;

    private CartItem cartItem;
    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setPrice(100);
        product.setDiscountedPrice(80);

        user = new User();
        user.setId(1L);

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setProduct(product);
        cartItem.setUserId(1L);
    }

    @Test
    void testCreateCartItem() {
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItem createdCartItem = cartItemService.createCartItem(cartItem);

        assertNotNull(createdCartItem);
        assertEquals(1, createdCartItem.getQuantity());
        assertEquals(100, createdCartItem.getPrice());
        assertEquals(80, createdCartItem.getDiscountedPrice());

        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testUpdateCartItem() throws CartItemException, UserException {
        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.of(cartItem));
        when(userService.findUserById(anyLong())).thenReturn(user);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItem newCartItem = new CartItem();
        newCartItem.setQuantity(2);

        CartItem updatedCartItem = cartItemService.updateCartItem(1L, 1L, newCartItem);

        assertNotNull(updatedCartItem);
        assertEquals(2, updatedCartItem.getQuantity());
        assertEquals(200, updatedCartItem.getPrice());
        assertEquals(160, updatedCartItem.getDiscountedPrice());

        verify(cartItemRepository, times(1)).findById(anyLong());
        verify(userService, times(1)).findUserById(anyLong());
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testRemoveCartItem() throws CartItemException, UserException {
        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.of(cartItem));
        when(userService.findUserById(anyLong())).thenReturn(user);

        cartItemService.removeCartItem(1L, 1L);

        verify(cartItemRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testFindCartItemById() throws CartItemException {
        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.of(cartItem));

        CartItem foundCartItem = cartItemService.findCartItemById(1L);

        assertNotNull(foundCartItem);
        assertEquals(1L, foundCartItem.getId());

        verify(cartItemRepository, times(1)).findById(anyLong());
    }

    @Test
    void testFindCartItemByIdNotFound() {
        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(CartItemException.class, () -> {
            cartItemService.findCartItemById(1L);
        });

        String expectedMessage = "cartItem not found with id : 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        verify(cartItemRepository, times(1)).findById(anyLong());
    }
}
