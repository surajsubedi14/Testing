package com.suraj.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.suraj.exception.OrderException;
import com.suraj.modal.Order;
import com.suraj.response.ApiResponse;
import com.suraj.service.OrderService;

class AdminOrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private AdminOrderController adminOrderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllOrdersHandler() {
        // Arrange
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> orders = Arrays.asList(order1, order2);
        when(orderService.getAllOrders()).thenReturn(orders);

        // Act
        ResponseEntity<List<Order>> response = adminOrderController.getAllOrdersHandler();

        // Assert
        assertEquals(ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void confirmedOrderHandler() throws OrderException {
        // Arrange
        Long orderId = 1L;
        Order confirmedOrder = new Order();
        when(orderService.confirmedOrder(orderId)).thenReturn(confirmedOrder);

        // Act
        ResponseEntity<Order> response = adminOrderController.ConfirmedOrderHandler(orderId, "Bearer token");

        // Assert
        assertEquals(ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(orderService, times(1)).confirmedOrder(orderId);
    }

    @Test
    void shippedOrderHandler() throws OrderException {
        // Arrange
        Long orderId = 2L;
        Order shippedOrder = new Order();
        when(orderService.shippedOrder(orderId)).thenReturn(shippedOrder);

        // Act
        ResponseEntity<Order> response = adminOrderController.shippedOrderHandler(orderId, "Bearer token");

        // Assert
        assertEquals(ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(orderService, times(1)).shippedOrder(orderId);
    }

    @Test
    void deliveredOrderHandler() throws OrderException {
        // Arrange
        Long orderId = 3L;
        Order deliveredOrder = new Order();
        when(orderService.deliveredOrder(orderId)).thenReturn(deliveredOrder);

        // Act
        ResponseEntity<Order> response = adminOrderController.deliveredOrderHandler(orderId, "Bearer token");

        // Assert
        assertEquals(ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(orderService, times(1)).deliveredOrder(orderId);
    }

    @Test
    void canceledOrderHandler() throws OrderException {
        // Arrange
        Long orderId = 4L;
        Order canceledOrder = new Order();
        when(orderService.cancledOrder(orderId)).thenReturn(canceledOrder);

        // Act
        ResponseEntity<Order> response = adminOrderController.canceledOrderHandler(orderId, "Bearer token");

        // Assert
        assertEquals(ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(orderService, times(1)).cancledOrder(orderId);
    }

    @Test
    void deleteOrderHandler() throws OrderException {
        // Arrange
        Long orderId = 5L;
        doNothing().when(orderService).deleteOrder(orderId);

        // Act
        ResponseEntity<ApiResponse> response = adminOrderController.deleteOrderHandler(orderId, "Bearer token");

        // Assert
        assertEquals(ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(orderService, times(1)).deleteOrder(orderId);
    }
}
