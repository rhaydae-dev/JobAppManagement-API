package com.rhaydae.dto;

import java.time.LocalDateTime;

import com.rhaydae.enums.ApplicationStatus;

public record ApplicationResponse(
		
			Long id,
			LocalDateTime appliedAt,
			ApplicationStatus status, 
			Long idCandidate, 
			Long idJobOffer
			)
	
{}
