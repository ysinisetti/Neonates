package com.neonates.service.implementation;

import com.neonates.entity.Case;
import com.neonates.exception.ResourceNotFoundException;
import com.neonates.mapper.CaseMapper;
import com.neonates.repository.CaseRepository;
import com.neonates.repository.HospitalRepository;
import com.neonates.request.CaseCreateRequestDTO;
import com.neonates.response.CaseResponseDTO;
import com.neonates.service.CaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CaseServiceImpl implements CaseService {

    private final CaseRepository caseRepository;
    private final HospitalRepository hospitalRepository;
    private final CaseMapper caseMapper;

    private static final AtomicLong caseReferenceCounter = new AtomicLong(1000);

    @Override
    public CaseResponseDTO createCase(CaseCreateRequestDTO dto) {
        // Validate hospital exists
        hospitalRepository.findById(dto.getHospitalId())
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + dto.getHospitalId()));

        // Map DTO to entity
        Case caseEntity = caseMapper.toEntity(dto);

        // Set default values
        caseEntity.setCaseReferenceNo(generateCaseReferenceNo());
        caseEntity.setIntakeDate(LocalDate.now());

        // Save and return
        Case savedCase = caseRepository.save(caseEntity);
        return caseMapper.toDTO(savedCase);
    }

    @Override
    public CaseResponseDTO getCaseById(Long caseId) {
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found with id: " + caseId));
        return caseMapper.toDTO(caseEntity);
    }

    @Override
    public void deleteCase(Long caseId) {
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found with id: " + caseId));
        caseRepository.delete(caseEntity);
    }

    private Long generateCaseReferenceNo() {
        return caseReferenceCounter.incrementAndGet();
    }
}
