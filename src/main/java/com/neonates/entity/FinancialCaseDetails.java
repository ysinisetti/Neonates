package com.neonates.entity;

import com.neonates.Enum.BplAplStatus;
import com.neonates.Enum.IncomeProofType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_case_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialCaseDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "financial_id")
    private Long financialId;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "income_proof_type")
    private IncomeProofType incomeProofType;

    @Column(name = "bank_statement_months_count", columnDefinition = "TINYINT")
    private Integer bankStatementMonthsCount;

    @Column(name = "economic_card_type")
    private String economicCardType;

    @Enumerated(EnumType.STRING)
    @Column(name = "bpl_apl_status")
    private BplAplStatus bplAplStatus;

    @Column(name = "advance_paid_amount", precision = 12, scale = 2)
    private BigDecimal advancePaidAmount;

    @Column(name = "medical_bill_estimate", precision = 12, scale = 2)
    private BigDecimal medicalBillEstimate;

    @Column(name = "estimated_nicu_days")
    private Integer estimatedNicuDays;

    @Column(name = "hospital_discount_amount", precision = 12, scale = 2)
    private BigDecimal hospitalDiscountAmount;

    @Column(name = "beneficiary_payable_balance", precision = 12, scale = 2)
    private BigDecimal beneficiaryPayableBalance;

    @Column(name = "reduction_amount", precision = 12, scale = 2)
    private BigDecimal reductionAmount;

    @Column(name = "reduction_notes", columnDefinition = "TEXT")
    private String reductionNotes;

    @Column(name = "sponsor_amount_quantified", precision = 12, scale = 2)
    private BigDecimal sponsorAmountQuantified;

    @Column(name = "sponsor_amount_final", precision = 12, scale = 2)
    private BigDecimal sponsorAmountFinal;

    @Column(name = "total_amount_approved", precision = 12, scale = 2)
    private BigDecimal totalAmountApproved;

    @Column(name = "financial_summary_snapshot", columnDefinition = "TEXT")
    private String financialSummarySnapshot;

    @Column(name = "manual_exception_notes", columnDefinition = "TEXT")
    private String manualExceptionNotes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        calculateFinancialFields();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        calculateFinancialFields();
    }

    private void calculateFinancialFields() {
        // Calculate beneficiary_payable_balance
        // (medical_bill_estimate - hospital_discount_amount - reduction_amount - sponsor_amount_final)
        if (this.medicalBillEstimate != null) {
            BigDecimal balance = this.medicalBillEstimate;
            if (this.hospitalDiscountAmount != null) {
                balance = balance.subtract(this.hospitalDiscountAmount);
            }
            if (this.reductionAmount != null) {
                balance = balance.subtract(this.reductionAmount);
            }
            if (this.sponsorAmountFinal != null) {
                balance = balance.subtract(this.sponsorAmountFinal);
            }
            this.beneficiaryPayableBalance = balance;
        }

        // Calculate total_amount_approved
        // (sponsor_amount_final + reduction_amount)
        BigDecimal totalApproved = BigDecimal.ZERO;
        if (this.sponsorAmountFinal != null) {
            totalApproved = totalApproved.add(this.sponsorAmountFinal);
        }
        if (this.reductionAmount != null) {
            totalApproved = totalApproved.add(this.reductionAmount);
        }
        this.totalAmountApproved = totalApproved;
    }
}
