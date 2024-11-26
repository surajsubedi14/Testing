package com.suraj.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.suraj.exception.ProductException;
import com.suraj.modal.Cart;
import com.suraj.modal.CartItem;
import com.suraj.modal.Product;
import com.suraj.modal.User;
import com.suraj.repository.CartRepository;
import com.suraj.request.AddItemRequest;

@ExtendWith(MockitoExtension.class)
class CartServiceImplementationTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemService cartItemService;

    @Mock
    private ProductService productService;

    private CartServiceImplementation cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartServiceImplementation(cartRepository, cartItemService, productService);
    }

    @Test
    void createCart_ShouldCreateNewCart() {
        // Arrange
        User user = new User();
        Cart expectedCart = new Cart();
        expectedCart.setUser(user);
        expectedCart.setCartItems(new HashSet<>());

        when(cartRepository.save(any(Cart.class))).thenReturn(expectedCart);

        // Act
        Cart resultCart = cartService.createCart(user);

        // Assert
        assertNotNull(resultCart);
        assertEquals(user, resultCart.getUser());
        assertTrue(resultCart.getCartItems().isEmpty());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void findUserCart_ShouldCalculateTotalsCorrectly() {
        // Arrange
        Long userId = 1L;
        Cart cart = new Cart();
        Set<CartItem> cartItems = new HashSet<>();

        CartItem item1 = new CartItem();
        item1.setPrice(100);
        item1.setDiscountedPrice(80);
        item1.setQuantity(2);

        CartItem item2 = new CartItem();
        item2.setPrice(150);
        item2.setDiscountedPrice(120);
        item2.setQuantity(1);

        cartItems.add(item1);
        cartItems.add(item2);
        cart.setCartItems(cartItems);

        when(cartRepository.findByUserId(userId)).thenReturn(cart);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // Act
        Cart resultCart = cartService.findUserCart(userId);

        // Assert
        assertEquals(250, resultCart.getTotalPrice()); // 100 + 150
        assertEquals(200, resultCart.getTotalDiscountedPrice()); // 80 + 120
        assertEquals(50, resultCart.getDiscounte()); // 250 - 200
        assertEquals(3, resultCart.getTotalItem()); // 2 + 1 quantities
        verify(cartRepository).findByUserId(userId);
        verify(cartRepository).save(cart);
    }

    @Test
    void addCartItem_WhenItemNotPresent_ShouldCreateNewCartItem() throws Exception {
        // Arrange
        Long userId = 1L;
        Long productId = 1L;
        String size = "M";
        int quantity = 2;

        AddItemRequest request = new AddItemRequest();
        request.setProductId(productId);
        request.setSize(size);
        request.setQuantity(quantity);

        Cart cart = new Cart();
        cart.setCartItems(new HashSet<>());

        Product product = new Product();
        product.setDiscountedPrice(100);

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setPrice(200); // quantity * discountedPrice
        newCartItem.setDiscountedPrice(quantity * product.getDiscountedPrice());

        when(cartRepository.findByUserId(userId)).thenReturn(cart);
        when(productService.findProductById(productId)).thenReturn(product);
        when(cartItemService.isCartItemExist(cart, product, size, userId)).thenReturn(null);
        when(cartItemService.createCartItem(any(CartItem.class))).thenReturn(newCartItem);

        // Act
        CartItem resultItem = cartService.addCartItem(userId, request);

        // Assert
        assertNotNull(resultItem);
        assertEquals(product, resultItem.getProduct());
        assertEquals(quantity, resultItem.getQuantity());
        assertEquals(200, resultItem.getPrice());
        verify(cartItemService).createCartItem(any(CartItem.class));
    }

    @Test
    void addCartItem_WhenItemPresent_ShouldReturnExistingItem() throws Exception {
        // Arrange
        Long userId = 1L;
        Long productId = 1L;
        String size = "M";
        int quantity = 2;

        AddItemRequest request = new AddItemRequest();
        request.setProductId(productId);
        request.setSize(size);
        request.setQuantity(quantity);

        Cart cart = new Cart();
        cart.setCartItems(new HashSet<>());
        Product product = new Product();
        CartItem existingItem = new CartItem();
        existingItem.setProduct(product);

        when(cartRepository.findByUserId(userId)).thenReturn(cart);
        when(productService.findProductById(productId)).thenReturn(product);
        when(cartItemService.isCartItemExist(cart, product, size, userId)).thenReturn(existingItem);

        // Act
        CartItem resultItem = cartService.addCartItem(userId, request);

        // Assert
        assertNotNull(resultItem);
        assertEquals(existingItem, resultItem);
        verify(cartItemService, never()).createCartItem(any(CartItem.class));
    }

    @Test
    void addCartItem_WhenProductNotFound_ShouldThrowException() throws Exception {
        // Arrange
        Long userId = 1L;
        Long productId = 1L;

        AddItemRequest request = new AddItemRequest();
        request.setProductId(productId);

        Cart cart = new Cart();
        cart.setCartItems(new HashSet<>());

        when(cartRepository.findByUserId(userId)).thenReturn(cart);
        when(productService.findProductById(productId)).thenThrow(new ProductException("Product not found"));

        // Act & Assert
        Exception exception = assertThrows(ProductException.class, () -> {
            cartService.addCartItem(userId, request);
        });

        assertEquals("Product not found", exception.getMessage());
        verify(cartItemService, never()).createCartItem(any(CartItem.class));
    }

    @Test
    void findUserCart_WithEmptyCart_ShouldReturnZeroTotals() {
        // Arrange
        Long userId = 1L;
        Cart cart = new Cart();
        cart.setCartItems(new HashSet<>());

        when(cartRepository.findByUserId(userId)).thenReturn(cart);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // Act
        Cart resultCart = cartService.findUserCart(userId);

        // Assert
        assertEquals(0, resultCart.getTotalPrice());
        assertEquals(0, resultCart.getTotalDiscountedPrice());
        assertEquals(0, resultCart.getDiscounte());
        assertEquals(0, resultCart.getTotalItem());
        verify(cartRepository).findByUserId(userId);
        verify(cartRepository).save(cart);
    }
}