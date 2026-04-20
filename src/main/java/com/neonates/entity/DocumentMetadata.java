package com.neonates.entity;

import com.neonates.Enum.DocumentCategory;
import com.neonates.Enum.DocumentSource;
import com.neonates.Enum.FileFormat;
import com.neonates.Enum.VisibilityScope;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "document_metadata")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "document_uuid", unique = true, length = 36, nullable = false)
    private String documentUuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id")
    private Case caseMaster;

    @Column(name = "bgrc_cycle_id")
    private Long bgrcCycleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_category", nullable = false)
    private DocumentCategory documentCategory;

    @Column(name = "document_type", nullable = false, length = 150)
    private String documentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_source", nullable = false)
    private DocumentSource documentSource = DocumentSource.Uploaded_File;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_format", nullable = false)
    private FileFormat fileFormat;

    @Column(name = "file_size_kb", nullable = false)
    private Integer fileSizeKb;

    @Column(name = "storage_path", nullable = false, length = 500)
    private String storagePath;

    @Column(name = "blob_name", length = 255)
    private String blobName;

    @Column(name = "checksum_sha256", length = 64)
    private String checksumSha256;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    private AppUser uploadedBy;

    @CreationTimestamp
    @Column(name = "uploaded_at", updatable = false, nullable = false)
    private LocalDateTime uploadedAt;

    @Column(name = "immutable_flag", nullable = false)
    private Boolean immutableFlag = true;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by")
    private AppUser verifiedBy;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "verification_notes", columnDefinition = "TEXT")
    private String verificationNotes;

    @Column(name = "is_locked", nullable = false)
    private Boolean isLocked = false;

    @Column(name = "locked_reason", length = 300)
    private String lockedReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility_scope", nullable = false)
    private VisibilityScope visibilityScope = VisibilityScope.All_Internal;

    @Column(name = "active_flag", nullable = false)
    private Boolean activeFlag = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (documentUuid == null) {
            documentUuid = UUID.randomUUID().toString();
        }
    }
}
