package com.neonates.response;

import com.neonates.Enum.CaseStatus;
import com.neonates.Enum.ProcessType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CaseResponseDTO {
    private Long caseId;
    private String caseUuid;
    private Long caseReferenceNo;
    private Long hospitalId;
    private ProcessType processType;
    private CaseStatus caseStatus;
    private String statusNfiLevel;
    private String statusPanelLevel;
    private String caseCategory;
    private LocalDate intakeDate;
    private LocalDate approvalDate;
    private LocalDate rejectionDate;
    private LocalDate closureDate;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
