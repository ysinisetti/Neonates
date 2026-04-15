package com.neonates.entity;

import com.neonates.Enum.CaseStatus;
import com.neonates.Enum.ProcessType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "case_master")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Case {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "case_id")
    private Long caseId;

    @Column(name = "case_uuid", unique = true, length = 36, nullable = false)
    private String caseUuid;

    @Column(name = "case_reference_no", unique = true)
    private Long caseReferenceNo;

    @Column(name = "hospital_id", nullable = false)
    private Long hospitalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "process_type", nullable = false)
    private ProcessType processType;

    @Enumerated(EnumType.STRING)
    @Column(name = "case_status", nullable = false)
    private CaseStatus caseStatus = CaseStatus.Draft;

    @Column(name = "status_nfi_level")
    private String statusNfiLevel;

    @Column(name = "status_panel_level")
    private String statusPanelLevel;

    @Column(name = "case_category")
    private String caseCategory;

    @Column(name = "intake_date")
    private LocalDate intakeDate;

    @Column(name = "approval_date")
    private LocalDate approvalDate;

    @Column(name = "rejection_date")
    private LocalDate rejectionDate;

    @Column(name = "closure_date")
    private LocalDate closureDate;

    @Column(name = "created_by")
    private Long createdBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (caseUuid == null) {
            caseUuid = UUID.randomUUID().toString();
        }
        if (caseStatus == null) {
            caseStatus = CaseStatus.Draft;
        }
        if (intakeDate == null) {
            intakeDate = LocalDate.now();
        }
    }
}
