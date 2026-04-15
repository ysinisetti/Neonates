package com.neonates.service;

import com.neonates.request.ClinicalCaseCreateRequest;
import com.neonates.response.ClinicalCaseResponse;

import java.util.Optional;

public interface ClinicalCaseDetailsService {

    ClinicalCaseResponse createClinicalCaseDetails(ClinicalCaseCreateRequest request);

    Optional<ClinicalCaseResponse> getClinicalDetailsByCaseId(Long caseId);

    ClinicalCaseResponse updateClinicalCaseDetails(Long clinicalId, ClinicalCaseCreateRequest request);
}
