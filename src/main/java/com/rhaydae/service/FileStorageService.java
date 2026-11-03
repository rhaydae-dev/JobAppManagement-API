
package com.rhaydae.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rhaydae.enums.DocumentType;

@Service
public class FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    // Save a file
    public String storeFile(MultipartFile file, DocumentType documentType) {

        // Check if the file is empty
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        // Check the file extension
        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null
                || !originalFileName.toLowerCase().endsWith(".pdf")) {
            throw new RuntimeException("Only PDF files are allowed");
        }

        // Check the file size (maximum 5 MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("File size must not exceed 5 MB");
        }

        try {

            // Choose the folder according to the document type
            String folder;

            if (documentType == DocumentType.CV) {
                folder = "cvs";
            } else {
                folder = "documents";
            }

            // Create the folder if it does not exist
            Path directory = Paths.get(uploadDir, folder);

            Files.createDirectories(directory);

            // Generate a unique file name
            String fileName = UUID.randomUUID().toString() + ".pdf";

            // Create the complete file path
            Path filePath = directory.resolve(fileName);

            // Save the file
            Files.copy(file.getInputStream(), filePath);

            // Return the relative path
            return folder + "/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Error while saving the file", e);
        }
    }

    // Delete a file
    public void deleteFile(String filePath) {

        try {

            Path path = Paths.get(uploadDir, filePath);

            Files.deleteIfExists(path);

        } catch (IOException e) {
            throw new RuntimeException("Error while deleting the file", e);
        }
    }
    
 // Load a file
    public byte[] loadFile(String filePath) {

        try {

            Path path = Paths.get(uploadDir, filePath);

            return Files.readAllBytes(path);

        } catch (IOException e) {

            throw new RuntimeException("File not found", e);
        }
    }
}
