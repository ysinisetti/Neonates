package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "report_template_binding")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportTemplateBinding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "binding_id")
    private Long bindingId;

    @Column(name = "template_id", nullable = false)
    private Long templateId;

    @Column(name = "dataset_id", nullable = false)
    private Long datasetId;

    @Column(name = "kpi_id")
    private Long kpiId;

    @Column(name = "binding_notes", columnDefinition = "TEXT")
    private String bindingNotes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
