package com.suraj.service;

import java.util.List;

import com.suraj.exception.ProductException;
import com.suraj.modal.Rating;
import com.suraj.modal.User;
import com.suraj.request.RatingRequest;

public interface RatingServices {
	
	public Rating createRating(RatingRequest req,User user) throws ProductException;
	
	public List<Rating> getProductsRating(Long productId);

}
