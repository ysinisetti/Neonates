package com.neonates.service;

import com.neonates.request.CaseCreateRequestDTO;
import com.neonates.response.CaseResponseDTO;

public interface CaseService {

    CaseResponseDTO createCase(CaseCreateRequestDTO dto);

    CaseResponseDTO getCaseById(Long caseId);
}
