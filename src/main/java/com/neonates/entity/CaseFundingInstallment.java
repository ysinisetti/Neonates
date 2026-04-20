package com.neonates.entity;

import com.neonates.Enum.FundingSource;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "case_funding_installment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseFundingInstallment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "funding_id")
    private Long fundingId;

    @Column(name = "decision_id", nullable = false)
    private Long decisionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "funding_source", nullable = false)
    private FundingSource fundingSource;

    @Column(name = "installment_no", nullable = false)
    private Integer installmentNo;

    @Column(name = "amount_approved", nullable = false, precision = 12, scale = 2)
    private BigDecimal amountApproved;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
