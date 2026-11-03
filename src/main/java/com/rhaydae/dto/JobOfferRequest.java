package com.rhaydae.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record JobOfferRequest(
		
		@NotBlank(message = "Title is required") 
		@Size(max = 150) 
		String title, 
		
		@NotBlank(message = "Company is required") 
		@Size(max = 100) 
		String company, 
		
		@NotBlank(message = "Location is required") 
		@Size(max = 100) 
		String location, 
		
		@Size(max = 2000) 
		String description,
		
		@NotBlank(message = "Contract type is required") 
		@Size(max = 100) 
		String contractType
		
		) {

}
