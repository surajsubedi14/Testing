package com.suraj.service;

import com.suraj.modal.OrderItem;
import com.suraj.repository.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderItemServiceImplementationTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderItemServiceImplementation orderItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrderItem_SavesOrderItem_ReturnsSavedOrderItem() {
        // Arrange
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setPrice(100); // Add other properties as necessary

        when(orderItemRepository.save(orderItem)).thenReturn(orderItem);

        // Act
        OrderItem savedOrderItem = orderItemService.createOrderItem(orderItem);

        // Assert
        assertNotNull(savedOrderItem);
        assertEquals(1L, savedOrderItem.getId());
        assertEquals(100, savedOrderItem.getPrice());

        verify(orderItemRepository, times(1)).save(orderItem);
    }

    @Test
    void createOrderItem_NullOrderItem_ThrowsException() {
        // Arrange
        OrderItem nullOrderItem = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            orderItemService.createOrderItem(nullOrderItem);
        });

        verify(orderItemRepository, never()).save(any());
    }
}
