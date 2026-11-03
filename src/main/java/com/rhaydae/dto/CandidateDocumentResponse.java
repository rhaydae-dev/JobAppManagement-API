package com.rhaydae.dto;

import java.time.LocalDateTime;

import com.rhaydae.enums.DocumentType;

public record CandidateDocumentResponse(
		Long id,
	    String originalFileName,
	    String contentType,
	    Long fileSize,
	    DocumentType documentType,
	    LocalDateTime uploadedAt
	    
		) {

}
