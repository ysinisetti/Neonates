package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fact_donation_txn")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactDonationTxn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "donation_txn_id")
    private Long donationTxnId;

    @Column(name = "date_id", nullable = false)
    private Integer dateId;

    @Column(name = "donor_category_code", nullable = false, length = 50)
    private String donorCategoryCode;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "reference_no", length = 100)
    private String referenceNo;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
