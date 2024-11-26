package com.suraj.service;

import org.springframework.stereotype.Service;

import com.suraj.modal.OrderItem;
import com.suraj.repository.OrderItemRepository;

@Service
public class OrderItemServiceImplementation implements OrderItemService {

	private OrderItemRepository orderItemRepository;
	public OrderItemServiceImplementation(OrderItemRepository orderItemRepository) {
		this.orderItemRepository=orderItemRepository;
	}
	@Override
	public OrderItem createOrderItem(OrderItem orderItem) {
		if (orderItem == null) {
			throw new IllegalArgumentException("OrderItem cannot be null");
		}
		
		return orderItemRepository.save(orderItem);
	}

}
