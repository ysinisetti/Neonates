package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "followup_metric_value")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowupMetricValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metric_value_id")
    private Long metricValueId;

    @Column(name = "followup_id", nullable = false)
    private Long followupId;

    @Column(name = "metric_def_id", nullable = false)
    private Long metricDefId;

    @Column(name = "value_bool")
    private Boolean valueBool;

    @Column(name = "value_number", precision = 10, scale = 2)
    private BigDecimal valueNumber;

    @Column(name = "value_text", columnDefinition = "TEXT")
    private String valueText;

    @Column(name = "value_date")
    private LocalDate valueDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
