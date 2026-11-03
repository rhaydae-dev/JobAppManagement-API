package com.rhaydae.dto;

import java.time.LocalDateTime;

import com.rhaydae.enums.Role;

public record RegisterResponse(
		Long id,
		String firstName,
		String LastName,
		String email,
		Role role,
		LocalDateTime createdAt
		) {

}
