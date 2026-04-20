package com.neonates.entity;

import com.neonates.Enum.RejectionReasonCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "rejection_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RejectionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rejection_id")
    private Long rejectionId;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "rejection_reason_category", nullable = false)
    private RejectionReasonCategory rejectionReasonCategory;

    @Column(name = "rejection_level", length = 100)
    private String rejectionLevel;

    @Column(name = "rejection_communication_status", length = 100)
    private String rejectionCommunicationStatus;

    @Column(name = "referring_hospital", length = 200)
    private String referringHospital;

    @Column(name = "rejection_case_summary", columnDefinition = "TEXT")
    private String rejectionCaseSummary;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
