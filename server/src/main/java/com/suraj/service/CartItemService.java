package com.suraj.service;

import com.suraj.exception.CartItemException;
import com.suraj.exception.UserException;
import com.suraj.modal.Cart;
import com.suraj.modal.CartItem;
import com.suraj.modal.Product;

public interface CartItemService {
	
	public CartItem createCartItem(CartItem cartItem);
	
	public CartItem updateCartItem(Long userId, Long id,CartItem cartItem) throws CartItemException, UserException;
	
	public CartItem isCartItemExist(Cart cart,Product product,String size, Long userId);
	
	public void removeCartItem(Long userId,Long cartItemId) throws CartItemException, UserException;
	
	public CartItem findCartItemById(Long cartItemId) throws CartItemException;
	
}
