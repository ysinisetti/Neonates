package com.neonates.entity;

import com.neonates.Enum.DocumentCategory;
import com.neonates.Enum.ProcessType;
import com.neonates.Enum.VisibilityScope;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "document_requirement_template")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequirementTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "requirement_id")
    private Long requirementId;

    @Enumerated(EnumType.STRING)
    @Column(name = "process_type", nullable = false)
    private ProcessType processType;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_category", nullable = false)
    private DocumentCategory documentCategory;

    @Column(name = "document_type", nullable = false, length = 150)
    private String documentType;

    @Column(name = "mandatory_flag")
    private Boolean mandatoryFlag = false;

    @Column(name = "multiple_attachments_allowed")
    private Boolean multipleAttachmentsAllowed = false;

    @Column(name = "condition_notes", length = 500)
    private String conditionNotes;

    @Column(name = "folder_order")
    private Integer folderOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility_scope", nullable = false)
    private VisibilityScope visibilityScope = VisibilityScope.All_Internal;

    @Column(name = "active_flag")
    private Boolean activeFlag = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
