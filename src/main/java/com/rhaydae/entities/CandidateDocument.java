package com.rhaydae.entities;

import java.time.LocalDateTime;

import com.rhaydae.enums.DocumentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Setter @Getter
@Builder
public class CandidateDocument {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false)
	    private String originalFileName;

	    @Column(nullable = false, unique = true)
	    private String storedFileName;

	    @Column(nullable = false)
	    private String filePath;

	    @Column(nullable = false)
	    private String contentType;

	    @Column(nullable = false)
	    private Long fileSize;

	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false, length = 30)
	    private DocumentType documentType;

	    @Column(nullable = false)
	    private LocalDateTime uploadedAt;

	    @ManyToOne(fetch = FetchType.LAZY, optional = false)
	    @JoinColumn(name = "candidate_id", nullable = false)
	    private User candidate;

}
