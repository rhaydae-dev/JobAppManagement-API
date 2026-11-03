package com.rhaydae.dto;

import java.time.LocalDateTime;

import com.rhaydae.enums.Role;

public record UserResponse(
		Long id,
		String firstName,
		String lastName,
		String email,
		Role role,
		LocalDateTime createdAt) {

}
