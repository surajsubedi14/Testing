package com.suraj.controller;

import com.suraj.exception.OrderException;
import com.suraj.exception.UserException;
import com.suraj.modal.Address;
import com.suraj.modal.Order;
import com.suraj.modal.User;
import com.suraj.service.OrderService;
import com.suraj.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrderHandler() throws UserException {
        // Arrange
        Address shippingAddress = new Address("123 Street", "City", "State", "123456");
        User user = new User();
        user.setId(1L);
        Order order = new Order();
        when(userService.findUserProfileByJwt(anyString())).thenReturn(user);
        when(orderService.createOrder(any(User.class), any(Address.class))).thenReturn(order);

        // Act
        ResponseEntity<Order> response = orderController.createOrderHandler(shippingAddress, "jwtToken");

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order, response.getBody());
        verify(userService, times(1)).findUserProfileByJwt(anyString());
        verify(orderService, times(1)).createOrder(any(User.class), any(Address.class));
    }

    @Test
    void usersOrderHistoryHandler() throws UserException, OrderException {
        // Arrange
        User user = new User();
        user.setId(1L);
        List<Order> orders = new ArrayList<>();
        orders.add(new Order());
        orders.add(new Order());
        when(userService.findUserProfileByJwt(anyString())).thenReturn(user);
        when(orderService.usersOrderHistory(anyLong())).thenReturn(orders);

        // Act
        ResponseEntity<List<Order>> response = orderController.usersOrderHistoryHandler("jwtToken");

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(orders, response.getBody());
        verify(userService, times(1)).findUserProfileByJwt(anyString());
        verify(orderService, times(1)).usersOrderHistory(anyLong());
    }

    @Test
    void findOrderHandler() throws UserException, OrderException {
        // Arrange
        User user = new User();
        user.setId(1L);
        Order order = new Order();
        when(userService.findUserProfileByJwt(anyString())).thenReturn(user);
        when(orderService.findOrderById(anyLong())).thenReturn(order);

        // Act
        ResponseEntity<Order> response = orderController.findOrderHandler(1L, "jwtToken");

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(order, response.getBody());
        verify(userService, times(1)).findUserProfileByJwt(anyString());
        verify(orderService, times(1)).findOrderById(anyLong());
    }
}
