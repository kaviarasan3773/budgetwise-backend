package com.kaviarasan.budgetwise.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.kaviarasan.budgetwise.entity.User;
import com.kaviarasan.budgetwise.repository.UserRepository;

public class SecurityUtil {
	
	 @Autowired
	 private UserRepository userRepository;
	
	public User getCurrentLoggedInUser() {
		
		Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
		
		String loggedInUserEmail = authentication.getName();
		
		User loggedInUser =userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() ->new RuntimeException("User not found"));
		
		return loggedInUser;
	}

}
