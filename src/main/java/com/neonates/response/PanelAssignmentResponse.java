package com.neonates.response;

import com.neonates.Enum.AssignmentStatus;
import com.neonates.Enum.PanelType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PanelAssignmentResponse {
    private Long panelAssignmentId;
    private Long caseId;
    private PanelType panelType;
    private Long reviewerUserId;
    private Long assignedBy;
    private LocalDateTime assignedAt;
    private AssignmentStatus assignmentStatus;
    private String notes;
    private LocalDate deadline;
    private LocalDateTime updatedAt;
}
