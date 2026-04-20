package com.neonates.entity;

import com.neonates.Enum.DecisionContextType;
import com.neonates.Enum.Outcome;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "case_decision")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseDecision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "decision_id")
    private Long decisionId;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision_context_type", nullable = false)
    private DecisionContextType decisionContextType;

    @Column(name = "submission_date")
    private LocalDate submissionDate;

    @Column(name = "decision_date")
    private LocalDate decisionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "outcome", nullable = false)
    private Outcome outcome;

    @Column(name = "approved_amount_total", precision = 12, scale = 2)
    private BigDecimal approvedAmountTotal;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
