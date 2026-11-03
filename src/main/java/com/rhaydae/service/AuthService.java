package com.rhaydae.service;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rhaydae.dto.LoginRequest;
import com.rhaydae.dto.LoginResponse;
import com.rhaydae.dto.RegisterRequest;
import com.rhaydae.dto.RegisterResponse;
import com.rhaydae.dto.UserResponse;
import com.rhaydae.entities.User;
import com.rhaydae.enums.Role;
import com.rhaydae.exception.EmailAlreadyExistsException;
import com.rhaydae.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository; 
	private final PasswordEncoder passwordEncoder; 
	private final JwtService jwtService;
	
	public RegisterResponse register(RegisterRequest dto) { 
		if (userRepository.existsByEmail(dto.email())) { 
			throw new EmailAlreadyExistsException( "Email already exists" );
			} 
		
		User user = User.builder()
		.firstName(dto.firstName())
		.LastName(dto.lastName())
		.email(dto.email())
		.password(passwordEncoder.encode(dto.password())) 
		.role(Role.CANDIDATE) 
		.createdAt(LocalDateTime.now()) 
		.build(); 
		
		User savedUser = userRepository.save(user); 
		
		return new RegisterResponse(
				savedUser.getId(), 
				savedUser.getFirstName(), 
				savedUser.getLastName(), 
				savedUser.getEmail(), 
				savedUser.getRole(), 
				savedUser.getCreatedAt() ); 
		
	}
	
	
	public LoginResponse login(LoginRequest request) {
	    try {
	        Authentication authentication =
	                authenticationManager.authenticate(
	                        new UsernamePasswordAuthenticationToken(
	                                request.email(),
	                                request.password()
	                        )
	                );

	        UserDetails userDetails =
	                (UserDetails) authentication.getPrincipal();

	        String token = jwtService.generateToken(userDetails);

	        return new LoginResponse(token, "Bearer");

	    } catch (org.springframework.security.core.AuthenticationException e) {
	        System.err.println("Login failed: "
	                + e.getClass().getSimpleName());
	        System.err.println("Reason: " + e.getMessage());
	        throw e;
	    }
	}
	
}
