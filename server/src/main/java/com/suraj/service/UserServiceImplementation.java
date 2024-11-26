package com.suraj.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

//import com.suraj.config.JwtTokenProvider;
import com.suraj.exception.UserException;
import com.suraj.modal.User;
import com.suraj.repository.UserRepository;

@Service
public class UserServiceImplementation implements UserService {

	private UserRepository userRepository;
//	private JwtTokenProvider jwtTokenProvider;

	public UserServiceImplementation(UserRepository userRepository) {

		this.userRepository=userRepository;
//		this.jwtTokenProvider=jwtTokenProvider;

	}

	@Override
	public User findUserById(Long userId) throws UserException {
		Optional<User> user=userRepository.findById(userId);

		if(user.isPresent()){
			return user.get();
		}
		throw new UserException("user not found with id "+userId);
	}

	@Override
	public User findUserProfileByJwt(String jwt) throws UserException {
//		System.out.println("user service");
//		String email=jwtTokenProvider.getEmailFromJwtToken(jwt);
//
//		System.out.println("email"+email);
//
//		User user=userRepository.findByEmail(email);
//
//		if(user==null) {
//			throw new UserException("user not exist with email "+email);
//		}
//		System.out.println("email user"+user.getEmail());
//		return user;
        return null;
	}

//	@Override
//	public List<User> findAllCustomers() {
//		return null;
//	}

	@Override
	public List<User> findAllUsers() {
		// TODO Auto-generated method stub
		return userRepository.findAllByOrderByCreatedAtDesc();
	}

}
