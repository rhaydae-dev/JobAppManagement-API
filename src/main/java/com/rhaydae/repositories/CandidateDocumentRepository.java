package com.rhaydae.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rhaydae.entities.CandidateDocument;
import com.rhaydae.enums.DocumentType;

@Repository
public interface CandidateDocumentRepository extends JpaRepository<CandidateDocument, Long> {

	    List<CandidateDocument> findByCandidate_IdOrderByUploadedAtDesc(Long candidateId);

	    Optional<CandidateDocument> findByCandidate_IdAndDocumentType(
	            Long candidateId, DocumentType documentType);

	    boolean existsByCandidate_IdAndDocumentType(
	            Long candidateId, DocumentType documentType);
}
