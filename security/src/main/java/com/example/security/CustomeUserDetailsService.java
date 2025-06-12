package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomeUserDetailsService implements UserDetailsService{
	
	@Autowired
	private LoginMasterRepository repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LoginMaster data = repo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		
		System.out.println("Loaded user from DB: " + data.getUsername());
		
		UserDetails user = User
				.withUsername(data.getUsername())
				.password(data.getPassword())
				.roles(data.getRole().split(","))
				.build();
		
		return user;
	}

}
