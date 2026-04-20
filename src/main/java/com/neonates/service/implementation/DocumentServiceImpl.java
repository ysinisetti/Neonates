package com.neonates.service.implementation;

import com.neonates.Enum.DocumentCategory;
import com.neonates.Enum.FileFormat;
import com.neonates.Enum.DocumentSource;
import com.neonates.entity.AppUser;
import com.neonates.entity.Case;
import com.neonates.entity.DocumentMetadata;
import com.neonates.repository.AppUserRepository;
import com.neonates.repository.CaseRepository;
import com.neonates.repository.DocumentMetadataRepository;
import com.neonates.service.AzureBlobService;
import com.neonates.service.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentMetadataRepository documentRepository;

    @Autowired
    private AzureBlobService azureBlobService;

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private AppUserRepository userRepository;

    @Override
    @Transactional
    public DocumentMetadata uploadDocument(MultipartFile file, Long caseId, Long userId, DocumentCategory category, String documentType) throws IOException {
        log.info("Starting document upload process for caseId: {}, userId: {}", caseId, userId);
        
        Case caseMaster = caseRepository.findById(caseId)
                .orElseThrow(() -> {
                    log.error("Case not found with ID: {}", caseId);
                    return new RuntimeException("Case not found");
                });
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found");
                });

        String blobName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        log.debug("Uploading file to Azure with blobName: {}", blobName);
        String storagePath = azureBlobService.upload(category, blobName, file);
        log.info("File uploaded to Azure storage at: {}", storagePath);

        DocumentMetadata metadata = DocumentMetadata.builder()
                .caseMaster(caseMaster)
                .uploadedBy(user)
                .documentCategory(category)
                .documentType(documentType)
                .fileName(file.getOriginalFilename())
                .blobName(blobName)
                .fileFormat(getFileFormat(file.getOriginalFilename()))
                .fileSizeKb((int) (file.getSize() / 1024))
                .storagePath(storagePath)
                .documentSource(DocumentSource.Uploaded_File)
                .immutableFlag(true)
                .isVerified(false)
                .isLocked(false)
                .activeFlag(true)
                .build();

        DocumentMetadata savedMetadata = documentRepository.save(metadata);
        log.info("Document metadata saved with ID: {}", savedMetadata.getDocumentId());
        return savedMetadata;
    }

    @Override
    @Transactional
    public DocumentMetadata updateDocument(Long documentId, MultipartFile file) throws IOException {
        DocumentMetadata metadata = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (metadata.getImmutableFlag()) {
            throw new RuntimeException("Document is immutable and cannot be updated");
        }

        String blobName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String storagePath = azureBlobService.upload(metadata.getDocumentCategory(), blobName, file);
        
        metadata.setStoragePath(storagePath);
        metadata.setBlobName(blobName);
        metadata.setFileName(file.getOriginalFilename());
        metadata.setFileFormat(getFileFormat(file.getOriginalFilename()));
        metadata.setFileSizeKb((int) (file.getSize() / 1024));

        return documentRepository.save(metadata);
    }

    @Override
    @Transactional
    public void deleteDocument(Long documentId) {
        DocumentMetadata metadata = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        
        azureBlobService.delete(metadata.getDocumentCategory(), metadata.getBlobName());
        documentRepository.delete(metadata);
    }

    @Override
    public DocumentMetadata getDocument(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    @Override
    public List<DocumentMetadata> getDocumentsByCase(Long caseId) {
        return documentRepository.findByCaseMasterCaseId(caseId);
    }

    @Override
    public byte[] downloadDocument(Long documentId) {
        DocumentMetadata metadata = getDocument(documentId);
        return azureBlobService.download(metadata.getDocumentCategory(), metadata.getBlobName());
    }

    private FileFormat getFileFormat(String fileName) {
        if (fileName == null) return null;
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
        try {
            return FileFormat.valueOf(extension);
        } catch (IllegalArgumentException e) {
            return FileFormat.TXT; // Default or throw error
        }
    }
}
