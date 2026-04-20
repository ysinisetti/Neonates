package com.neonates.service;

import com.neonates.Enum.DocumentCategory;
import com.neonates.entity.DocumentMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocumentService {
    DocumentMetadata uploadDocument(MultipartFile file, Long caseId, Long userId, DocumentCategory category, String documentType) throws IOException;
    DocumentMetadata updateDocument(Long documentId, MultipartFile file) throws IOException;
    void deleteDocument(Long documentId);
    DocumentMetadata getDocument(Long documentId);
    List<DocumentMetadata> getDocumentsByCase(Long caseId);
    byte[] downloadDocument(Long documentId);
}
