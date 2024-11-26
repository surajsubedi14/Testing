package com.suraj.service;

import java.util.List;

import com.suraj.exception.UserException;
import com.suraj.modal.User;
import org.springframework.stereotype.Service;


public interface UserService {

	public User findUserById(Long userId) throws UserException;

	public User findUserProfileByJwt(String jwt) throws UserException;

	public List<User> findAllUsers();

}
