package com.rhaydae.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ApplicationRequest(
		
		   @NotNull(message = "Job offer ID is required")
		   @Positive(message = "Job offer ID must be positive")
		   Long jobOfferId
		
		) {

}
