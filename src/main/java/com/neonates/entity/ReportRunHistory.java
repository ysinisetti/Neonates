package com.neonates.entity;

import com.neonates.Enum.ReportRunStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "report_run_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRunHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "run_id")
    private Long runId;

    @Column(name = "template_id", nullable = false)
    private Long templateId;

    @Column(name = "parameters_json", columnDefinition = "JSON")
    private String parametersJson;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReportRunStatus status = ReportRunStatus.Queued;

    @Column(name = "data_as_of")
    private LocalDateTime dataAsOf;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    @Column(name = "generated_by")
    private Long generatedBy;

    @Column(name = "export_blob_id")
    private Long exportBlobId;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
