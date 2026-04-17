package com.neonates.service;

import com.neonates.entity.ClinicalDetails;
import com.neonates.mapper.ClinicalDetailsMapper;
import com.neonates.repository.ClinicalDetailsRepository;
import com.neonates.request.ClinicalDetailsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClinicalDetailsService {

    private final ClinicalDetailsRepository repository;
    private final ClinicalDetailsMapper mapper;

    @Transactional
    public ClinicalDetails saveOrUpdateClinicalDetails(ClinicalDetailsRequest request) {
        if (request.getCaseId() == null) {
            throw new IllegalArgumentException("CaseId is mandatory");
        }

        Optional<ClinicalDetails> existingOpt = repository.findByCaseId(request.getCaseId());
        ClinicalDetails details;

        if (existingOpt.isPresent()) {
            details = existingOpt.get();
            mapper.updateEntityFromRequest(request, details);
        } else {
            details = mapper.toEntity(request);
        }

        return repository.save(details);
    }

    public Optional<ClinicalDetails> getByCaseId(Long caseId) {
        return repository.findByCaseId(caseId);
    }

    @Transactional
    public ClinicalDetails updateClinicalDetails(Long clinicalId, ClinicalDetailsRequest request) {
        ClinicalDetails details = repository.findById(clinicalId)
                .orElseThrow(() -> new IllegalArgumentException("Clinical details not found for ID: " + clinicalId));
        
        mapper.updateEntityFromRequest(request, details);
        return repository.save(details);
    }
}
