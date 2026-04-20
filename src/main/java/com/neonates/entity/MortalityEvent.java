package com.neonates.entity;

import com.neonates.Enum.CaptureStage;
import com.neonates.Enum.MortalityConfirmedStatus;
import com.neonates.Enum.MortalitySourceType;
import com.neonates.Enum.WorkflowImpactStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "mortality_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MortalityEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mortality_event_id")
    private Long mortalityEventId;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "capture_stage", nullable = false)
    private CaptureStage captureStage;

    @Column(name = "observed_date")
    private LocalDate observedDate;

    @Column(name = "reported_by_user_id")
    private Long reportedByUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false)
    private MortalitySourceType sourceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "mortality_status", nullable = false)
    private MortalityConfirmedStatus mortalityStatus = MortalityConfirmedStatus.Confirmed;

    @Column(name = "place_of_death", length = 150)
    private String placeOfDeath;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "communication_completed_flag")
    private Boolean communicationCompletedFlag = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "workflow_impact_status", nullable = false)
    private WorkflowImpactStatus workflowImpactStatus = WorkflowImpactStatus.Needs_Review;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
