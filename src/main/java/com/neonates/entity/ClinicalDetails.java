package com.neonates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "clinical_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clinical_id")
    private Long clinicalId;

    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Column(name = "antenatal_risk_factors", columnDefinition = "TEXT")
    private String antenatalRiskFactors;

    @Column(name = "risk_notes")
    private String riskNotes;

    @Column(name = "diagnoses")
    private String diagnoses;

    @Column(name = "other_diagnosis")
    private String otherDiagnosis;

    @Column(name = "respiration_support")
    private String respirationSupport;

    @Column(name = "iv_antibiotics")
    @Builder.Default
    private Boolean ivAntibiotics = false;

    @Column(name = "ionotropes")
    @Builder.Default
    private Boolean ionotropes = false;

    @Column(name = "tpn")
    @Builder.Default
    private Boolean tpn = false;

    @Column(name = "other_treatment")
    private String otherTreatment;

    @Column(name = "current_day_of_life")
    private Integer currentDayOfLife;

    @Column(name = "current_weight_kg")
    private BigDecimal currentWeightKg;

    @Column(name = "corrected_gestational_age_weeks")
    private BigDecimal correctedGestationalAgeWeeks;

    @Column(name = "mechanical_ventilation")
    @Builder.Default
    private Boolean mechanicalVentilation = false;

    @Column(name = "cpap")
    @Builder.Default
    private Boolean cpap = false;

    @Column(name = "hfnc")
    @Builder.Default
    private Boolean hfnc = false;

    @Column(name = "oxygen_support")
    @Builder.Default
    private Boolean oxygenSupport = false;

    @Column(name = "npo")
    @Builder.Default
    private Boolean npo = false;

    @Column(name = "og_feeding")
    @Builder.Default
    private Boolean ogFeeding = false;

    @Column(name = "palada_feeding")
    @Builder.Default
    private Boolean paladaFeeding = false;

    @Column(name = "dbf_feeding")
    @Builder.Default
    private Boolean dbfFeeding = false;

    @Column(name = "other_feeding")
    private String otherFeeding;

    @Column(name = "discharge_plan")
    private String dischargePlan;

    @Column(name = "labs_attached")
    @Builder.Default
    private Boolean labsAttached = false;

    @Column(name = "xray_attached")
    @Builder.Default
    private Boolean xrayAttached = false;

    @Column(name = "scans_attached")
    @Builder.Default
    private Boolean scansAttached = false;

    @Column(name = "other_reports_attached")
    @Builder.Default
    private Boolean otherReportsAttached = false;

    @Column(name = "other_investigation_details")
    private String otherInvestigationDetails;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "signature_file_path")
    private String signatureFilePath;

    @Column(name = "signed_date")
    private LocalDateTime signedDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
