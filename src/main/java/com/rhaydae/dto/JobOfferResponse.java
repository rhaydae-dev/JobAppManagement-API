package com.rhaydae.dto;

import java.time.LocalDateTime;

public record JobOfferResponse(
		
		Long id, 
		String title, 
		String company, 
		String location, 
		String description, 
		String contractType,
		LocalDateTime createdAt,
		boolean active
		) {

}
