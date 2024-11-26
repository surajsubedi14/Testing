package com.suraj.service;

import com.suraj.exception.ProductException;
import com.suraj.modal.Cart;
import com.suraj.modal.CartItem;
import com.suraj.modal.User;
import com.suraj.request.AddItemRequest;

public interface CartService {
	
	public Cart createCart(User user);
	
	public CartItem addCartItem(Long userId,AddItemRequest req) throws ProductException;
	
	public Cart findUserCart(Long userId);

}
