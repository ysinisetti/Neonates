package com.neonates.entity;

import com.neonates.Enum.ExportVisibilityScope;
import com.neonates.Enum.FileFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "export_blob")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportBlob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "export_blob_id")
    private Long exportBlobId;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_format", nullable = false)
    private FileFormat fileFormat;

    @Column(name = "storage_path", nullable = false, length = 500)
    private String storagePath;

    @Column(name = "checksum_sha256", length = 64)
    private String checksumSha256;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility_scope", nullable = false)
    private ExportVisibilityScope visibilityScope = ExportVisibilityScope.Internal_Only;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}
