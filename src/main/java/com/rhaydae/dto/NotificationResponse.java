package com.rhaydae.dto;

import java.time.LocalDateTime;

public record NotificationResponse(
		Long id,
		String message,
	    Boolean read,
		LocalDateTime createdAt
		) {

}
