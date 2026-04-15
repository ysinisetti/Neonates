package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fact_balance_snapshot")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "balance_snapshot_id")
    private Long balanceSnapshotId;

    @Column(name = "date_id", nullable = false, unique = true)
    private Integer dateId;

    @Column(name = "bank_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal bankAmount;

    @Column(name = "fd_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal fdAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "captured_by", nullable = false)
    private AppUser capturedBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
