package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "clinical_case_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalCaseDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clinical_id")
    private Long clinicalId;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Column(name = "primary_diagnosis", nullable = false)
    private String primaryDiagnosis;

    @Column(name = "case_summary_0", columnDefinition = "TEXT")
    private String caseSummary0;

    @Column(name = "case_summary_1", columnDefinition = "TEXT")
    private String caseSummary1;

    @Column(name = "case_summary_2", columnDefinition = "TEXT")
    private String caseSummary2;

    @Column(name = "case_summary_3", columnDefinition = "TEXT")
    private String caseSummary3;

    @Column(name = "apgar_score")
    private String apgarScore;

    @Column(name = "time_of_birth")
    private LocalTime timeOfBirth;

    @Column(name = "place_of_birth")
    private String placeOfBirth;

    @Column(name = "maternal_age_years", precision = 5, scale = 2)
    private BigDecimal maternalAgeYears;

    @Column(name = "gravida")
    private Integer gravida;

    @Column(name = "para")
    private Integer para;

    @Column(name = "abortion_count")
    private Integer abortionCount;

    @Column(name = "living_children_count")
    private Integer livingChildrenCount;

    @Column(name = "antenatal_risk_factors", columnDefinition = "TEXT")
    private String antenatalRiskFactors;

    @Column(name = "diagnosis_checklist", columnDefinition = "TEXT")
    private String diagnosisChecklist;

    @Column(name = "treatment_given", columnDefinition = "TEXT")
    private String treatmentGiven;

    @Column(name = "current_day_of_life")
    private Integer currentDayOfLife;

    @Column(name = "corrected_gestational_age_weeks", precision = 5, scale = 2)
    private BigDecimal correctedGestationalAgeWeeks;

    @Column(name = "feeding_mode")
    private String feedingMode;

    @Column(name = "respiration_mode")
    private String respirationMode;

    @Column(name = "discharge_plan", columnDefinition = "TEXT")
    private String dischargePlan;

    @Column(name = "investigation_attached_flag", columnDefinition = "TINYINT(1)")
    private Boolean investigationAttachedFlag;

    @Column(name = "interim_summary_complete_flag", columnDefinition = "TINYINT(1)")
    private Boolean interimSummaryCompleteFlag;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.investigationAttachedFlag == null) {
            this.investigationAttachedFlag = false;
        }
        if (this.interimSummaryCompleteFlag == null) {
            this.interimSummaryCompleteFlag = false;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
