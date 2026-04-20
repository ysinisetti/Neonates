package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fact_case_daily_snapshot")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactCaseDailySnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "snapshot_id")
    private Long snapshotId;

    @Column(name = "date_id", nullable = false)
    private Integer dateId;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Column(name = "hospital_id", nullable = false)
    private Long hospitalId;

    @Column(name = "status_code", nullable = false, length = 50)
    private String statusCode;

    @Column(name = "open_flag")
    private Boolean openFlag = false;

    @Column(name = "approved_amount", precision = 12, scale = 2)
    private BigDecimal approvedAmount;

    @CreationTimestamp
    @Column(name = "computed_at", updatable = false)
    private LocalDateTime computedAt;
}
