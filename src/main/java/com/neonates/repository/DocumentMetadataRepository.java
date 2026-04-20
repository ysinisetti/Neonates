package com.neonates.repository;

import com.neonates.entity.DocumentMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentMetadataRepository extends JpaRepository<DocumentMetadata, Long> {
    Optional<DocumentMetadata> findByDocumentUuid(String documentUuid);
    List<DocumentMetadata> findByCaseMasterCaseId(Long caseId);
}
