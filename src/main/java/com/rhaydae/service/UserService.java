package com.rhaydae.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rhaydae.dto.CreateRecruiterRequest;
import com.rhaydae.dto.UserRequest;
import com.rhaydae.dto.UserResponse;
import com.rhaydae.entities.User;
import com.rhaydae.enums.Role;
import com.rhaydae.exception.EmailAlreadyExistsException;
import com.rhaydae.exception.UserNotFoundException;
import com.rhaydae.repositories.UserRepository;
import com.rhaydae.dto.ActivateAccountRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	final UserRepository userRepository;
	final PasswordEncoder passwordEncoder;
	final EmailService emailService;
	
	//create recruiter by admin	
	public UserResponse createRecruiter(CreateRecruiterRequest dto) {
	    if (userRepository.existsByEmail(dto.email())) {
	        throw new EmailAlreadyExistsException("Email already exists");
	    }

	    String token = java.util.UUID.randomUUID().toString();

	    User user = User.builder()
	            .firstName(dto.firstName())
	            .LastName(dto.lastName())
	            .email(dto.email())
	            .password(passwordEncoder.encode(
	                    java.util.UUID.randomUUID().toString()))
	            .role(Role.RECRUITER)
	            .createdAt(LocalDateTime.now())
	            .enabled(false)
	            .activationToken(token)
	            .activationTokenExpiry(LocalDateTime.now().plusMinutes(30))
	            .build();

	    User savedUser = userRepository.save(user);

	    emailService.sendActivationEmail(savedUser.getEmail(), token);

	    return new UserResponse(
	            savedUser.getId(),
	            savedUser.getFirstName(),
	            savedUser.getLastName(),
	            savedUser.getEmail(),
	            savedUser.getRole(),
	            savedUser.getCreatedAt()
	    );
	}
	
	
	//activation recruter account
	public void activateAccount(ActivateAccountRequest dto) {
	    User user = userRepository.findByActivationToken(dto.token())
	            .orElseThrow(() ->
	                    new IllegalArgumentException("Invalid activation token"));

	    if (user.getActivationTokenExpiry() == null
	            || user.getActivationTokenExpiry().isBefore(LocalDateTime.now())) {
	        throw new IllegalArgumentException("Activation token has expired");
	    }

	    if (user.isEnabled()) {
	        throw new IllegalArgumentException("Account is already activated");
	    }

	    user.setPassword(passwordEncoder.encode(dto.password()));
	    user.setEnabled(true);
	    user.setActivationToken(null);
	    user.setActivationTokenExpiry(null);

	    userRepository.save(user);
	}
	
	//get user by id
	public UserResponse getUserById(Long id) {
		User user = userRepository.findById(id) 
				.orElseThrow(() -> new UserNotFoundException("User not found with id: " + id) ); 
		
		return toResponse(user);
	}
	
	//get All users
	public List<UserResponse> getAllUseres(){
		return userRepository.findAll()
				.stream()
				.map(this :: toResponse)
				.toList();
		
	}
	
	//update a new user
	public UserResponse updateUser(Long id, UserRequest dto) {
		
		User user = userRepository.findById(id)
				.orElseThrow(()-> new UserNotFoundException("id n'existe pas"));
		
		
		if (!user.getEmail().equals(dto.email()) && userRepository.existsByEmail(dto.email())) {
		 throw new EmailAlreadyExistsException("Email already exists");
		 }
			
		user.setFirstName(dto.firstName());
		user.setLastName(dto.lastName());
		user.setEmail(dto.email());
		user.setPassword(passwordEncoder.encode(dto.password()));
		user.setRole(dto.role());
		user.setCreatedAt(LocalDateTime.now());
		
		User savedUser = userRepository.save(user);
		
		return toResponse(savedUser);
	}
	
	// delete user 
	public void deleteUser(Long id) {
		
		if (!userRepository.existsById(id)) {
			throw new UserNotFoundException( "User not found with id: " + id ); 
			}
		
		 userRepository.deleteById(id);
	}
	
	// Convert Entity to Response DTO 
	private UserResponse toResponse(User user){
		return new UserResponse(
				 user.getId(), 
				 user.getFirstName(), 
				 user.getLastName(), 
				 user.getEmail(), 
				 user.getRole(),
				 user.getCreatedAt()
				 );
		}

	
}
