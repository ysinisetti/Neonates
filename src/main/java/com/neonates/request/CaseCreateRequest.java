package com.neonates.request;

import com.neonates.Enum.ProcessType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CaseCreateRequest {
    @NotNull(message = "Hospital ID is mandatory")
    private Long hospitalId;

    @NotNull(message = "Process type is mandatory")
    private ProcessType processType;

    private String statusNfiLevel;
    private String statusPanelLevel;
    private String caseCategory;

    @NotNull(message = "Created by is mandatory")
    private Long createdBy;
}
