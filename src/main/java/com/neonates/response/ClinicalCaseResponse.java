package com.neonates.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class ClinicalCaseResponse {

    private Long clinicalId;
    private Long caseId;
    private String primaryDiagnosis;
    private String caseSummary0;
    private String caseSummary1;
    private String caseSummary2;
    private String caseSummary3;
    private String apgarScore;
    private LocalTime timeOfBirth;
    private String placeOfBirth;
    private BigDecimal maternalAgeYears;
    private Integer gravida;
    private Integer para;
    private Integer abortionCount;
    private Integer livingChildrenCount;
    private String antenatalRiskFactors;
    private String diagnosisChecklist;
    private String treatmentGiven;
    private Integer currentDayOfLife;
    private BigDecimal correctedGestationalAgeWeeks;
    private String feedingMode;
    private String respirationMode;
    private String dischargePlan;
    private Boolean investigationAttachedFlag;
    private Boolean interimSummaryCompleteFlag;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
