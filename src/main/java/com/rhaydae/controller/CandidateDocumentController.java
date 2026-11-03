
package com.rhaydae.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rhaydae.dto.CandidateDocumentResponse;
import com.rhaydae.enums.DocumentType;
import com.rhaydae.service.CandidateDocumentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class CandidateDocumentController {

    private final CandidateDocumentService documentService;

    // Upload a CV or an additional document
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(security = {
            @SecurityRequirement(name = "bearerAuth")
        })
    public CandidateDocumentResponse uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("documentType") DocumentType documentType,
            Authentication authentication) {

        String email = authentication.getName();

        return documentService.uploadDocument(file, documentType, email);
    }

    // Get all documents of the connected candidate
    @GetMapping("/myDocuments")
    @Operation(security = {
            @SecurityRequirement(name = "bearerAuth")
        })
    public List<CandidateDocumentResponse> getMyDocuments(
            Authentication authentication) {

        String email = authentication.getName();

        return documentService.getMyDocuments(email);
    }

    // Delete a document
    @DeleteMapping("/delete/{id}")
    @Operation(security = {
            @SecurityRequirement(name = "bearerAuth")
        })
    public String deleteDocument(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        documentService.deleteDocument(id, email);

        return "Document deleted successfully";
    }
    
 // Download a document
    @GetMapping("/download/{id}")
    @Operation(security = {
            @SecurityRequirement(name = "bearerAuth")
        })
    public ResponseEntity<byte[]> downloadDocument(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        byte[] file = documentService.downloadDocument(id, email);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"document.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(file);
    }
}
