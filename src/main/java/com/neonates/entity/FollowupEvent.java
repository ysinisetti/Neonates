package com.neonates.entity;

import com.neonates.Enum.FollowupStatus;
import com.neonates.Enum.Milestone;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "followup_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowupEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "followup_id")
    private Long followupId;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "milestone_months", nullable = false)
    private Milestone milestoneMonths;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "followup_status", nullable = false)
    private FollowupStatus followupStatus = FollowupStatus.Due;

    @Column(name = "reached_flag")
    private Boolean reachedFlag = false;

    @Column(name = "reached_at")
    private LocalDateTime reachedAt;

    @Column(name = "conducted_by")
    private Long conductedBy;

    @Column(name = "mortality_observed_flag")
    private Boolean mortalityObservedFlag = false;

    @Column(name = "mortality_event_id")
    private Long mortalityEventId;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
