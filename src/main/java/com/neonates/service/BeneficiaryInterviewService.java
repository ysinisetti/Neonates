package com.neonates.service;

import com.neonates.response.BeneficiaryInterviewResponse;
import com.neonates.request.BeneficiaryInterviewCreateRequest;

public interface BeneficiaryInterviewService {
    BeneficiaryInterviewResponse createInterview(BeneficiaryInterviewCreateRequest request);
    BeneficiaryInterviewResponse updateInterview(Long id, BeneficiaryInterviewCreateRequest request);
    BeneficiaryInterviewResponse getInterviewByCaseId(Long caseId);
}
