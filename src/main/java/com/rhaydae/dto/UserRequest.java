package com.rhaydae.dto;

import com.rhaydae.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(
		
		@NotBlank(message = "First Name is required")
		@Size(max = 50)
		String firstName,
		@NotBlank(message = "Last Name is required")
		@Size(max = 50)
		String lastName,
		@NotBlank(message = "Email is required")
		@Email(message = "Invalid email format")
		String email,
		@NotBlank(message = "Password is required")
		@Size(min = 8, message = "Password must contain at least 8 characters")
		String password,
		@NotNull(message = "Role is required")
		Role role
		
		) {

}
