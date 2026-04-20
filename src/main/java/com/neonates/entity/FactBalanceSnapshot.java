package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fact_balance_snapshot")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactBalanceSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "balance_snapshot_id")
    private Long balanceSnapshotId;

    @Column(name = "date_id", nullable = false)
    private Integer dateId;

    @Column(name = "bank_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal bankAmount;

    @Column(name = "fd_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal fdAmount;

    @Column(name = "captured_by")
    private Long capturedBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
