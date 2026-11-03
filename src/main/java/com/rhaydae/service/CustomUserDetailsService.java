package com.rhaydae.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rhaydae.entities.User;
import com.rhaydae.enums.Role;
import com.rhaydae.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
	
	 @Value("${app.admin.email}")
	 private String adminEmail;

     @Value("${app.admin.password}")
     private String adminPassword;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		 if (adminEmail != null && adminEmail.equalsIgnoreCase(email)) {
	            return org.springframework.security.core.userdetails.User
	                    .withUsername(adminEmail)
	                    .password(passwordEncoder.encode(adminPassword))	                
	                    .roles(Role.ADMIN.name())
	                    .build();
	        }
		
		User user = userRepository.findByEmail(email)
				.orElseThrow(
						() -> new UsernameNotFoundException("User not found with email" +email)
						);
		
		
		return org.springframework.security.core.userdetails.User
				.withUsername(user.getEmail())
				.password(user.getPassword())
				.roles(user.getRole().name())
		        .disabled(!user.isEnabled())
				.build();
						
				
	}

}
