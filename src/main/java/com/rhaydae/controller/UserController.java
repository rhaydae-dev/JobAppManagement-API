package com.rhaydae.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhaydae.dto.CreateRecruiterRequest;
import com.rhaydae.dto.UserRequest;
import com.rhaydae.dto.UserResponse;
import com.rhaydae.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/users")
public class UserController {
	
	private final UserService userService;
	
	@PostMapping("/createRecruiter")
	public UserResponse createRecruter(@Valid @RequestBody CreateRecruiterRequest dto) {
		return userService.createRecruiter(dto);
	}
	
	@GetMapping("/getBy/{id}")
	public UserResponse getUserById(@PathVariable Long id) {
		return userService.getUserById(id);
	}
	
	@GetMapping("/AllUsers")
	public List<UserResponse> getAllUsers(){
		return userService.getAllUseres();
		}
	
	@PutMapping("/updateUser/{id}")
	public UserResponse updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest dto) {
		return userService.updateUser(id, dto);
	}
	
	@DeleteMapping("/delete/{id}")
	public void deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
	}
}
