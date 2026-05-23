package com.kaviarasan.budgetwise.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.kaviarasan.budgetwise.entity.User;
import com.kaviarasan.budgetwise.repository.UserRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> findByEmail = userRepo.findByEmail(email);
		if(findByEmail == null) {
			throw new UsernameNotFoundException("User not found");
		}
		
		return new UserPrincipal(findByEmail.get());
	}

}
