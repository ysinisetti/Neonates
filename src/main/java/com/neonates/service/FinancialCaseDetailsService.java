package com.neonates.service;

import com.neonates.request.FinancialCaseCreateRequest;
import com.neonates.response.FinancialCaseResponse;

import java.util.Optional;

public interface FinancialCaseDetailsService {

    FinancialCaseResponse createFinancialCaseDetails(FinancialCaseCreateRequest request);

    Optional<FinancialCaseResponse> getFinancialDetailsByCaseId(Long caseId);

    FinancialCaseResponse updateFinancialCaseDetails(Long financialId, FinancialCaseCreateRequest request);
}
