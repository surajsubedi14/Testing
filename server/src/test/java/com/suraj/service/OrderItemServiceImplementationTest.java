package com.suraj.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.suraj.modal.OrderItem;
import com.suraj.repository.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
//        orderItem.setName("Sample Item");
        orderItem.setPrice(100);

        when(orderItemRepository.save(orderItem)).thenReturn(orderItem);

        // Act
        OrderItem savedOrderItem = orderItemService.createOrderItem(orderItem);

        // Assert
        assertNotNull(savedOrderItem);
        assertEquals(1L, savedOrderItem.getId());
//        assertEquals("Sample Item", savedOrderItem.getName());
        assertEquals(100, savedOrderItem.getPrice());

        verify(orderItemRepository, times(1)).save(orderItem);
    }
}
