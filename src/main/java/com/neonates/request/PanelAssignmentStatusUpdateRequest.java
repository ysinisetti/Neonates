package com.neonates.request;

import com.neonates.Enum.AssignmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PanelAssignmentStatusUpdateRequest {

    @NotNull(message = "Assignment status is mandatory")
    private AssignmentStatus assignmentStatus;

    private String notes;
}
