package com.neonates.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
public class ClinicalCaseCreateRequest {

    @NotNull(message = "Case ID is mandatory")
    private Long caseId;

    @NotBlank(message = "Primary diagnosis is mandatory")
    private String primaryDiagnosis;

    private String caseSummary0;
    private String caseSummary1;
    private String caseSummary2;
    private String caseSummary3;

    private String apgarScore;

    private LocalTime timeOfBirth;

    private String placeOfBirth;

    @DecimalMin(value = "10.0", message = "Maternal age must be at least 10")
    @DecimalMax(value = "60.0", message = "Maternal age must be at most 60")
    private BigDecimal maternalAgeYears;

    @Min(value = 0, message = "Gravida must be non-negative")
    private Integer gravida;

    @Min(value = 0, message = "Para must be non-negative")
    private Integer para;

    @Min(value = 0, message = "Abortion count must be non-negative")
    private Integer abortionCount;

    @Min(value = 0, message = "Living children count must be non-negative")
    private Integer livingChildrenCount;

    private String antenatalRiskFactors;
    private String diagnosisChecklist;
    private String treatmentGiven;

    private BigDecimal correctedGestationalAgeWeeks;
    private String feedingMode;
    private String respirationMode;
    private String dischargePlan;

    private Boolean investigationAttachedFlag;
    private Boolean interimSummaryCompleteFlag;

    private String remarks;
}
