package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "agg_accounts_daily")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggAccountsDaily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agg_accounts_daily_id")
    private Long aggAccountsDailyId;

    @Column(name = "date_id", nullable = false)
    private Integer dateId;

    @Column(name = "completed_babies_till_date")
    private Integer completedBabiesTillDate;

    @Column(name = "completed_babies_current_month")
    private Integer completedBabiesCurrentMonth;

    @Column(name = "inquiries_count")
    private Integer inquiriesCount;

    @Column(name = "rejected_count")
    private Integer rejectedCount;

    @Column(name = "in_pipeline_count")
    private Integer inPipelineCount;

    @Column(name = "partner_hospital_completed_count")
    private Integer partnerHospitalCompletedCount;

    @Column(name = "partner_hospital_pipeline_count")
    private Integer partnerHospitalPipelineCount;

    @Column(name = "funds_raised_mtd", precision = 12, scale = 2)
    private BigDecimal fundsRaisedMtd;

    @Column(name = "funds_raised_daily", precision = 12, scale = 2)
    private BigDecimal fundsRaisedDaily;

    @Column(name = "total_funds", precision = 12, scale = 2)
    private BigDecimal totalFunds;

    @Column(name = "committed_outflow_amount", precision = 12, scale = 2)
    private BigDecimal committedOutflowAmount;

    @Column(name = "surplus_funds", precision = 12, scale = 2)
    private BigDecimal surplusFunds;

    @Column(name = "program_ops_ratio", precision = 10, scale = 4)
    private BigDecimal programOpsRatio;

    @CreationTimestamp
    @Column(name = "computed_at", updatable = false)
    private LocalDateTime computedAt;
}
