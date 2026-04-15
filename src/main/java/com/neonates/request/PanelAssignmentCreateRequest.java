package com.neonates.request;

import com.neonates.Enum.AssignmentStatus;
import com.neonates.Enum.PanelType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PanelAssignmentCreateRequest {
    @NotNull(message = "Case ID is mandatory")
    private Long caseId;
    @NotNull(message = "Panel type is mandatory")
    private PanelType panelType;
    @NotNull(message = "Reviewer user ID is mandatory")
    private Long reviewerUserId;
    @NotNull(message = "Assigned by is mandatory")
    private Long assignedBy;
    private String notes;
    private LocalDate deadline;
}
