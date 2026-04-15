package com.neonates.entity;

import com.neonates.Enum.AssignmentStatus;
import com.neonates.Enum.PanelType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "panel_assignment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PanelAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "panel_assignment_id")
    private Long panelAssignmentId;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "panel_type", nullable = false)
    private PanelType panelType;

    @Column(name = "reviewer_user_id", nullable = false)
    private Long reviewerUserId;

    @Column(name = "assigned_by", nullable = false)
    private Long assignedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "assignment_status", nullable = false)
    private AssignmentStatus assignmentStatus;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.assignedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.assignmentStatus == null) {
            this.assignmentStatus = AssignmentStatus.Assigned;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
