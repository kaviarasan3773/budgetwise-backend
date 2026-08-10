package com.kaviarasan.budgetwise.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kaviarasan.budgetwise.dto.LoginRequest;
import com.kaviarasan.budgetwise.dto.RegisterRequest;
import com.kaviarasan.budgetwise.entity.User;
import com.kaviarasan.budgetwise.exception.EmailAlreadyExistsException;
import com.kaviarasan.budgetwise.exception.InvalidCredentialsException;
import com.kaviarasan.budgetwise.exception.UserInactiveException;
import com.kaviarasan.budgetwise.repository.UserRepository;
import com.kaviarasan.budgetwise.security.JwtUtil;

@Service
public class AuthService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	// default security in spring security
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	public String registerUser(RegisterRequest request) {

	    if (userRepo.existsByEmail(request.getEmail())) {
	        throw new EmailAlreadyExistsException("Email already registered.");
	    }

	    User user = new User();

	    user.setUserName(request.getUserName());
	    user.setEmail(request.getEmail());
	    user.setPassword(encoder.encode(request.getPassword()));
	    user.setRole("USER");
	    user.setStatus("ACTIVE");
	    user.setCreatedDate(LocalDateTime.now());

	    userRepo.save(user);

	    // Automatically log the user in after successful registration
	    return jwtUtil.generateToken(user.getEmail());
	}

    public String validateUser(LoginRequest request) {
    	
    	Optional<User> optionalUser = userRepo.findByEmail(request.getEmail());
    	if(optionalUser.isEmpty())
    		throw new InvalidCredentialsException("Invalid Email or Password");
    	
    	User user = optionalUser.get();
    	
    	boolean passwordMatch = encoder.matches(request.getPassword(), user.getPassword());
    	if(!passwordMatch)
    		throw new InvalidCredentialsException("Invalid Email or Password");
    	
    	if(!"ACTIVE".equalsIgnoreCase(user.getStatus()))
    		throw new UserInactiveException("User Account Is Inactive"); ;
    	
    	return jwtUtil.generateToken(user.getEmail());
    }
    
}