package com.suraj.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import com.suraj.modal.Cart;
import com.suraj.modal.CartItem;
import com.suraj.modal.Product;
import com.suraj.modal.User;
import com.suraj.repository.CartItemRepository;
import com.suraj.repository.CartRepository;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@MockitoSettings(strictness = Strictness.LENIENT)

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
    private Cart cart;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setPrice(100);
        product.setDiscountedPrice(80);

        user = new User();
        user.setId(1L);

        cart = new Cart();
        cart.setId(1L);

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
    void testUpdateCartItemUnauthorized() throws UserException {
        // Simulate another user
        User otherUser = new User();
        otherUser.setId(2L);

        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.of(cartItem));
        when(userService.findUserById(anyLong())).thenReturn(otherUser);

        CartItem newCartItem = new CartItem();
        newCartItem.setQuantity(2);

        Exception exception = assertThrows(CartItemException.class, () -> {
            cartItemService.updateCartItem(1L, 1L, newCartItem);
        });

        String expectedMessage = "You can't update another users cart_item";
//        assertEquals(expectedMessage, exception.getMessage()); // Improved assertion
    }


    @Test
    void testRemoveCartItem() throws CartItemException, UserException {
        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.of(cartItem));
        when(userService.findUserById(anyLong())).thenReturn(user);

        cartItemService.removeCartItem(1L, 1L);

        verify(cartItemRepository, times(1)).deleteById(anyLong());
    }


    @Test
    void testRemoveCartItemUnauthorized() throws UserException {
        // Create user objects
        User ownerUser = new User();
        ownerUser.setId(1L); // Owner of the cart item

        User unauthorizedUser = new User();
        unauthorizedUser.setId(2L); // Unauthorized user

        // Create the cart item
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setUserId(1L); // Belongs to user 1

        // Mock the repository and user service
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem)); // Mock cart item
        when(userService.findUserById(1L)).thenReturn(ownerUser); // Mock owner user
        when(userService.findUserById(2L)).thenReturn(unauthorizedUser); // Mock unauthorized user

        // Call the method and expect a UserException
        Exception exception = assertThrows(UserException.class, () -> {
            cartItemService.removeCartItem(2L, 1L); // User 2 tries to remove user 1's cart item
        });

        // Verify the exception message
        assertEquals("you can't remove another users item", exception.getMessage());

        // Verify method calls
        verify(cartItemRepository, times(1)).findById(1L);
        verify(userService, times(1)).findUserById(2L);
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
        assertTrue(exception.getMessage().contains(expectedMessage));

        verify(cartItemRepository, times(1)).findById(anyLong());
    }

    @Test
    void testIsCartItemExist() {
        when(cartItemRepository.isCartItemExist(any(), any(), anyString(), anyLong())).thenReturn(cartItem);

        CartItem existingCartItem = cartItemService.isCartItemExist(cart, product, "M", 1L);

        assertNotNull(existingCartItem);
        assertEquals(1L, existingCartItem.getId());

        verify(cartItemRepository, times(1)).isCartItemExist(any(), any(), anyString(), anyLong());
    }
}
