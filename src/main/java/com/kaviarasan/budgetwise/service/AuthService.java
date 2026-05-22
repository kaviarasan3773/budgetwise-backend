package com.kaviarasan.budgetwise.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kaviarasan.budgetwise.dto.LoginRequest;
import com.kaviarasan.budgetwise.dto.RegisterRequest;
import com.kaviarasan.budgetwise.entity.User;
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

	    if(userRepo.existsByEmail(request.getEmail())) {
	        return "Email already exists";
	    }
	    User user = new User();

	    user.setUserName(request.getUserName());
	    user.setEmail(request.getEmail());
	    user.setPassword(encoder.encode(request.getPassword()));
	    user.setRole("USER");
	    user.setStatus("ACTIVE");
	    user.setCreatedDate(LocalDateTime.now());
	    userRepo.save(user);
	    return "Registration Successful";
	}

    public String validateUser(LoginRequest request) {
    	
    	Optional<User> optionalUser = userRepo.findByEmail(request.getEmail());
    	if(optionalUser.isEmpty())
    		return "Invalid Credentials";
    	
    	User user = optionalUser.get();
    	
    	boolean passwordMatch = encoder.matches(request.getPassword(), user.getPassword());
    	if(!passwordMatch)
    		return "Invalid Credentials";
    	
    	if(!"ACTIVE".equalsIgnoreCase(user.getStatus()))
    		return "User Account Is Inactive";
    	
    	return jwtUtil.generateToken(user.getEmail());
    }
    
}