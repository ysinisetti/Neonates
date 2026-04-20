package com.neonates.entity;

import com.neonates.Enum.ConsolidationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "panel_consolidation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PanelConsolidation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "panel_consolidation_id")
    private Long panelConsolidationId;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Column(name = "clinical_status", length = 50)
    private String clinicalStatus;

    @Column(name = "social_status", length = 50)
    private String socialStatus;

    @Column(name = "financial_status", length = 50)
    private String financialStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "consolidation_status", nullable = false)
    private ConsolidationStatus consolidationStatus = ConsolidationStatus.Pending;

    @Column(name = "summary_notes", columnDefinition = "TEXT")
    private String summaryNotes;

    @Column(name = "last_evaluated_at")
    private LocalDateTime lastEvaluatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
