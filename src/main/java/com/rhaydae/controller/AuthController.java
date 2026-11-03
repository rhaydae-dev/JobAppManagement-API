package com.rhaydae.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhaydae.dto.LoginRequest;
import com.rhaydae.dto.LoginResponse;
import com.rhaydae.dto.RegisterRequest;
import com.rhaydae.dto.RegisterResponse;
import com.rhaydae.service.AuthService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor 
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService; 
	
	@PostMapping("/register") 
	public ResponseEntity<RegisterResponse> register( @Valid @RequestBody RegisterRequest request) { 
		return ResponseEntity.ok( authService.register(request) );
		}
	
	@PostMapping("/login") 
	public ResponseEntity<LoginResponse> login( @Valid @RequestBody LoginRequest request) { 
		return ResponseEntity.ok( authService.login(request) );
		
	}
}
