package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "workflow_status_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workflow_id")
    private Long workflowId;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Column(name = "from_status", length = 100)
    private String fromStatus;

    @Column(name = "to_status", nullable = false, length = 100)
    private String toStatus;

    @Column(name = "changed_by", nullable = false)
    private Long changedBy;

    @Column(name = "actor_role", length = 100)
    private String actorRole;

    @Column(name = "change_reason", length = 300)
    private String changeReason;

    @CreationTimestamp
    @Column(name = "changed_at", updatable = false)
    private LocalDateTime changedAt;
}
