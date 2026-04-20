package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "case_donor_map")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseDonorMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "case_donor_id")
    private Long caseDonorId;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Column(name = "donor_id")
    private Long donorId;

    @Column(name = "sponsor_label", length = 200)
    private String sponsorLabel;

    @Column(name = "funding_source_label", length = 100)
    private String fundingSourceLabel;

    @Column(name = "mapped_amount", precision = 12, scale = 2)
    private BigDecimal mappedAmount;

    @Column(name = "allocation_notes", columnDefinition = "TEXT")
    private String allocation_notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
