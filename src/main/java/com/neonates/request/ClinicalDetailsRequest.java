package com.neonates.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalDetailsRequest {
    private Long caseId;
    private String antenatalRiskFactors;
    private String riskNotes;
    private String diagnoses;
    private String otherDiagnosis;
    private String respirationSupport;
    private Boolean ivAntibiotics;
    private Boolean ionotropes;
    private Boolean tpn;
    private String otherTreatment;
    private Integer currentDayOfLife;
    private BigDecimal currentWeightKg;
    private BigDecimal correctedGestationalAgeWeeks;
    private Boolean mechanicalVentilation;
    private Boolean cpap;
    private Boolean hfnc;
    private Boolean oxygenSupport;
    private Boolean npo;
    private Boolean ogFeeding;
    private Boolean paladaFeeding;
    private Boolean dbfFeeding;
    private String otherFeeding;
    private String dischargePlan;
    private Boolean labsAttached;
    private Boolean xrayAttached;
    private Boolean scansAttached;
    private Boolean otherReportsAttached;
    private String otherInvestigationDetails;
    private String remarks;
    private String signatureFilePath;
    private LocalDateTime signedDate;
}
