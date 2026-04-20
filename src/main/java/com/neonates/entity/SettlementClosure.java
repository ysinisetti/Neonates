package com.neonates.entity;

import com.neonates.Enum.DirectorReviewStatus;
import com.neonates.Enum.ReferenceAmountBasis;
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
@Table(name = "settlement_closure")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettlementClosure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "settlement_id")
    private Long settlementId;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Column(name = "discharge_date")
    private LocalDate dischargeDate;

    @Column(name = "final_bill_amount", precision = 12, scale = 2)
    private BigDecimal finalBillAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_amount_basis", nullable = false)
    private ReferenceAmountBasis referenceAmountBasis;

    @Column(name = "variance_pct", precision = 8, scale = 4)
    private BigDecimal variancePct;

    @Column(name = "variance_flag")
    private Boolean varianceFlag = false;

    @Column(name = "director_review_required_flag")
    private Boolean directorReviewRequiredFlag = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "director_review_status", nullable = false)
    private DirectorReviewStatus directorReviewStatus = DirectorReviewStatus.Not_Required;

    @Column(name = "closure_ready_flag")
    private Boolean closureReadyFlag = false;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
