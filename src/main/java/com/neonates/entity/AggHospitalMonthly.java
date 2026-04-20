package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "agg_hospital_monthly")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggHospitalMonthly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agg_hospital_monthly_id")
    private Long aggHospitalMonthlyId;

    @Column(name = "hospital_id", nullable = false)
    private Long hospitalId;

    @Column(name = "fiscal_year", nullable = false)
    private Integer fiscalYear;

    @Column(name = "fiscal_month", nullable = false)
    private Integer fiscalMonth;

    @Column(name = "enquiries_count", nullable = false)
    private Integer enquiriesCount = 0;

    @Column(name = "approved_count", nullable = false)
    private Integer approvedCount = 0;

    @Column(name = "rejected_count", nullable = false)
    private Integer rejectedCount = 0;

    @Column(name = "conversion_ratio", precision = 10, scale = 4)
    private BigDecimal conversionRatio;

    @Column(name = "carry_forward_count")
    private Integer carryForwardCount;

    @CreationTimestamp
    @Column(name = "computed_at", updatable = false)
    private LocalDateTime computedAt;
}
