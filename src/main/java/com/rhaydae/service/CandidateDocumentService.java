
package com.rhaydae.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.rhaydae.dto.CandidateDocumentResponse;
import com.rhaydae.entities.CandidateDocument;
import com.rhaydae.entities.User;
import com.rhaydae.enums.DocumentType;
import com.rhaydae.repositories.CandidateDocumentRepository;
import com.rhaydae.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CandidateDocumentService {

    private final CandidateDocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    // Upload a CV or an additional document
    public CandidateDocumentResponse uploadDocument(
            MultipartFile file, DocumentType documentType, String email) {

        // Find the candidate
        User candidate = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // If the document is a CV, check if the candidate already has one
        if (documentType == DocumentType.CV) {

            CandidateDocument existingCV = documentRepository
                    .findByCandidate_IdAndDocumentType(
                            candidate.getId(), DocumentType.CV)
                    .orElse(null);

            // Delete the old CV before saving the new one
            if (existingCV != null) {
                fileStorageService.deleteFile(existingCV.getFilePath());
                documentRepository.delete(existingCV);
            }
        }

        // Save the file in the uploads folder
        String filePath = fileStorageService.storeFile(file, documentType);

        // Create the document information
        CandidateDocument document = CandidateDocument.builder()
                .originalFileName(file.getOriginalFilename())
                .storedFileName(filePath.substring(filePath.lastIndexOf("/") + 1))
                .filePath(filePath)
                .contentType(file.getContentType())
                .fileSize(file.getSize())
                .documentType(documentType)
                .uploadedAt(LocalDateTime.now())
                .candidate(candidate)
                .build();

        // Save the document information in MySQL
        CandidateDocument savedDocument = documentRepository.save(document);

        return toResponse(savedDocument);
    }

    // Get all documents belonging to the connected candidate
    @Transactional(readOnly = true)
    public List<CandidateDocumentResponse> getMyDocuments(String email) {

        User candidate = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return documentRepository
                .findByCandidate_IdOrderByUploadedAtDesc(candidate.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Delete one of the candidate's documents
    public void deleteDocument(Long documentId, String email) {

        User candidate = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CandidateDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        // Check that the document belongs to the connected candidate
        if (!document.getCandidate().getId().equals(candidate.getId())) {
            throw new RuntimeException("You cannot delete this document");
        }

        // Delete the physical file
        fileStorageService.deleteFile(document.getFilePath());

        // Delete the document information from MySQL
        documentRepository.delete(document);
    }

    // Convert an entity into a response DTO
    private CandidateDocumentResponse toResponse(CandidateDocument document) {

        return new CandidateDocumentResponse(
                document.getId(),
                document.getOriginalFileName(),
                document.getContentType(),
                document.getFileSize(),
                document.getDocumentType(),
                document.getUploadedAt()
        );
    }
    
 // Download a document
    public byte[] downloadDocument(Long documentId, String email) {

        // Find the candidate
        User candidate = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find the document
        CandidateDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        // Check if the document belongs to the candidate
        if (!document.getCandidate().getId().equals(candidate.getId())) {
            throw new RuntimeException("You cannot download this document");
        }

        // Download the physical file
        return fileStorageService.loadFile(document.getFilePath());
    }
}

