package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "agg_org_monthly")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggOrgMonthly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agg_org_monthly_id")
    private Long aggOrgMonthlyId;

    @Column(name = "fiscal_year", nullable = false)
    private Integer fiscalYear;

    @Column(name = "fiscal_month", nullable = false)
    private Integer fiscalMonth;

    @Column(name = "enquiries_count", nullable = false)
    private Integer enquiriesCount = 0;

    @Column(name = "approved_count", nullable = false)
    private Integer approvedCount = 0;

    @Column(name = "identified_hospitals_count")
    private Integer identifiedHospitalsCount;

    @Column(name = "active_hospitals_count")
    private Integer activeHospitalsCount;

    @Column(name = "donation_total", precision = 12, scale = 2)
    private BigDecimal donationTotal;

    @Column(name = "program_cost", precision = 12, scale = 2)
    private BigDecimal programCost;

    @Column(name = "ops_cost", precision = 12, scale = 2)
    private BigDecimal opsCost;

    @Column(name = "program_ops_ratio", precision = 10, scale = 4)
    private BigDecimal programOpsRatio;

    @CreationTimestamp
    @Column(name = "computed_at", updatable = false)
    private LocalDateTime computedAt;
}
