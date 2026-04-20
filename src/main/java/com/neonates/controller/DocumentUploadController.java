package com.neonates.controller;

import com.neonates.Enum.DocumentCategory;
import com.neonates.entity.DocumentMetadata;
import com.neonates.service.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/documents")
public class DocumentUploadController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<DocumentMetadata> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("caseId") Long caseId,
            @RequestParam("userId") Long userId,
            @RequestParam("category") DocumentCategory category,
            @RequestParam("documentType") String documentType) {
        log.info("Received upload request for caseId: {}, userId: {}, category: {}, documentType: {}, fileName: {}", 
                caseId, userId, category, documentType, file.getOriginalFilename());
        try {
            DocumentMetadata metadata = documentService.uploadDocument(file, caseId, userId, category, documentType);
            log.info("Successfully uploaded document. DocumentId: {}", metadata.getDocumentId());
            return new ResponseEntity<>(metadata, HttpStatus.CREATED);
        } catch (IOException e) {
            log.error("Error uploading document for caseId: {}. Error: {}", caseId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<DocumentMetadata> updateDocument(
            @PathVariable Long documentId,
            @RequestParam("file") MultipartFile file) {
        log.info("Received update request for documentId: {}, new fileName: {}", documentId, file.getOriginalFilename());
        try {
            DocumentMetadata metadata = documentService.updateDocument(documentId, file);
            log.info("Successfully updated document. DocumentId: {}", documentId);
            return new ResponseEntity<>(metadata, HttpStatus.OK);
        } catch (IOException e) {
            log.error("IO Error updating documentId: {}. Error: {}", documentId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            log.error("Runtime Error updating documentId: {}. Error: {}", documentId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long documentId) {
        documentService.deleteDocument(documentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<DocumentMetadata> getDocument(@PathVariable Long documentId) {
        return new ResponseEntity<>(documentService.getDocument(documentId), HttpStatus.OK);
    }

    @GetMapping("/case/{caseId}")
    public ResponseEntity<List<DocumentMetadata>> getDocumentsByCase(@PathVariable Long caseId) {
        return new ResponseEntity<>(documentService.getDocumentsByCase(caseId), HttpStatus.OK);
    }

    @GetMapping("/download/{documentId}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long documentId) {
        DocumentMetadata metadata = documentService.getDocument(documentId);
        byte[] data = documentService.downloadDocument(documentId);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + metadata.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }
}
