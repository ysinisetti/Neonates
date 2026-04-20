package com.neonates.entity;

import com.neonates.Enum.Milestone;
import com.neonates.Enum.ValueType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "followup_metric_definition")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowupMetricDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metric_def_id")
    private Long metricDefId;

    @Enumerated(EnumType.STRING)
    @Column(name = "milestone_months", nullable = false)
    private Milestone milestoneMonths;

    @Column(name = "template_global_index")
    private Integer templateGlobalIndex;

    @Column(name = "template_column_key", length = 50)
    private String templateColumnKey;

    @Column(name = "metric_label", nullable = false, length = 255)
    private String metricLabel;

    @Enumerated(EnumType.STRING)
    @Column(name = "value_type", nullable = false)
    private ValueType valueType;

    @Column(name = "active_flag")
    private Boolean activeFlag = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
