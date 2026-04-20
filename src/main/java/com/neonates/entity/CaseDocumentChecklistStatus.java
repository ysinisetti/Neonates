package com.neonates.entity;

import com.neonates.Enum.ChecklistStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "case_document_checklist_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseDocumentChecklistStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checklist_id")
    private Long checklistId;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Column(name = "requirement_id", nullable = false)
    private Long requirementId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ChecklistStatus status = ChecklistStatus.Missing;

    @Column(name = "satisfied_by_document_id")
    private Long satisfiedByDocumentId;

    @Column(name = "last_updated_by", nullable = false)
    private Long lastUpdatedBy;

    @UpdateTimestamp
    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;
}
