package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "report_kpi_def")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportKpiDef {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kpi_id")
    private Long kpiId;

    @Column(name = "kpi_code", unique = true, nullable = false, length = 100)
    private String kpiCode;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "formula_expression", nullable = false, columnDefinition = "TEXT")
    private String formulaExpression;

    @Column(name = "grain", length = 100)
    private String grain;

    @Column(name = "active_flag")
    private Boolean activeFlag = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
