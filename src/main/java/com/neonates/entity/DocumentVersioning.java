package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "document_versioning")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentVersioning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_version_id")
    private Long documentVersionId;

    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Column(name = "version_no", nullable = false)
    private Integer versionNo;

    @Column(name = "supersedes_version_id")
    private Long supersedesVersionId;

    @Column(name = "storage_path", nullable = false, length = 500)
    private String storagePath;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "file_size_kb", nullable = false)
    private Integer fileSizeKb;

    @Column(name = "checksum_sha256", length = 64)
    private String checksumSha256;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
