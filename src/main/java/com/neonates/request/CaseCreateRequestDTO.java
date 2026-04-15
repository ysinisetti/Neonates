package com.neonates.request;

import com.neonates.Enum.ProcessType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CaseCreateRequestDTO {

    @NotNull(message = "Hospital ID is required")
    private Long hospitalId;

    @NotNull(message = "Process type is required")
    private ProcessType processType;

    private String caseCategory;

    private String statusNfiLevel;

    private String statusPanelLevel;

    private Long createdBy;
}
