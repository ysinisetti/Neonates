package com.neonates.service.implementation;

import com.neonates.entity.Case;
import com.neonates.entity.ClinicalCaseDetails;
import com.neonates.exception.ResourceNotFoundException;
import com.neonates.mapper.ClinicalCaseDetailsMapper;
import com.neonates.repository.CaseRepository;
import com.neonates.repository.ClinicalCaseDetailsRepository;
import com.neonates.request.ClinicalCaseCreateRequest;
import com.neonates.response.ClinicalCaseResponse;
import com.neonates.service.ClinicalCaseDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClinicalCaseDetailsServiceImpl implements ClinicalCaseDetailsService {

    private final ClinicalCaseDetailsRepository clinicalCaseDetailsRepository;
    private final ClinicalCaseDetailsMapper clinicalCaseDetailsMapper;
    private final CaseRepository caseRepository;

    @Override
    public ClinicalCaseResponse createClinicalCaseDetails(ClinicalCaseCreateRequest request) {
        log.info("Creating clinical case details for case ID: {}", request.getCaseId());

        // Validate case_id exists
        Case caseEntity = caseRepository.findById(request.getCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Case not found with id: " + request.getCaseId()));

        // Validate para <= gravida
        if (request.getPara() != null && request.getGravida() != null && request.getPara() > request.getGravida()) {
            throw new IllegalArgumentException("Para cannot be greater than gravida");
        }

        // Map to entity
        ClinicalCaseDetails entity = clinicalCaseDetailsMapper.toEntity(request);

        // Calculate current day of life
        if (caseEntity.getIntakeDate() != null) {
            long days = ChronoUnit.DAYS.between(caseEntity.getIntakeDate(), LocalDate.now());
            entity.setCurrentDayOfLife((int) days);
        }

        // Save
        ClinicalCaseDetails saved = clinicalCaseDetailsRepository.save(entity);
        log.info("Clinical case details created with ID: {}", saved.getClinicalId());

        return clinicalCaseDetailsMapper.toResponse(saved);
    }

    @Override
    public Optional<ClinicalCaseResponse> getClinicalDetailsByCaseId(Long caseId) {
        log.info("Fetching clinical details for case ID: {}", caseId);
        return clinicalCaseDetailsRepository.findByCaseId(caseId)
                .map(clinicalCaseDetailsMapper::toResponse);
    }

    @Override
    public ClinicalCaseResponse updateClinicalCaseDetails(Long clinicalId, ClinicalCaseCreateRequest request) {
        log.info("Updating clinical case details with ID: {}", clinicalId);

        // Validate clinical details exist
        ClinicalCaseDetails entity = clinicalCaseDetailsRepository.findById(clinicalId)
                .orElseThrow(() -> new ResourceNotFoundException("Clinical case details not found with id: " + clinicalId));

        // Validate case_id exists
        Case caseEntity = caseRepository.findById(request.getCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Case not found with id: " + request.getCaseId()));

        // Validate para <= gravida
        if (request.getPara() != null && request.getGravida() != null && request.getPara() > request.getGravida()) {
            throw new IllegalArgumentException("Para cannot be greater than gravida");
        }

        // Update fields
        updateEntityFromRequest(request, entity);

        // Recalculate current day of life
        if (caseEntity.getIntakeDate() != null) {
            long days = ChronoUnit.DAYS.between(caseEntity.getIntakeDate(), LocalDate.now());
            entity.setCurrentDayOfLife((int) days);
        }

        // Save
        ClinicalCaseDetails updated = clinicalCaseDetailsRepository.save(entity);
        log.info("Clinical case details updated with ID: {}", updated.getClinicalId());

        return clinicalCaseDetailsMapper.toResponse(updated);
    }

    private void updateEntityFromRequest(ClinicalCaseCreateRequest request, ClinicalCaseDetails entity) {
        entity.setCaseId(request.getCaseId());
        entity.setPrimaryDiagnosis(request.getPrimaryDiagnosis());
        entity.setCaseSummary0(request.getCaseSummary0());
        entity.setCaseSummary1(request.getCaseSummary1());
        entity.setCaseSummary2(request.getCaseSummary2());
        entity.setCaseSummary3(request.getCaseSummary3());
        entity.setApgarScore(request.getApgarScore());
        entity.setTimeOfBirth(request.getTimeOfBirth());
        entity.setPlaceOfBirth(request.getPlaceOfBirth());
        entity.setMaternalAgeYears(request.getMaternalAgeYears());
        entity.setGravida(request.getGravida());
        entity.setPara(request.getPara());
        entity.setAbortionCount(request.getAbortionCount());
        entity.setLivingChildrenCount(request.getLivingChildrenCount());
        entity.setAntenatalRiskFactors(request.getAntenatalRiskFactors());
        entity.setDiagnosisChecklist(request.getDiagnosisChecklist());
        entity.setTreatmentGiven(request.getTreatmentGiven());
        entity.setCorrectedGestationalAgeWeeks(request.getCorrectedGestationalAgeWeeks());
        entity.setFeedingMode(request.getFeedingMode());
        entity.setRespirationMode(request.getRespirationMode());
        entity.setDischargePlan(request.getDischargePlan());
        entity.setInvestigationAttachedFlag(request.getInvestigationAttachedFlag());
        entity.setInterimSummaryCompleteFlag(request.getInterimSummaryCompleteFlag());
        entity.setRemarks(request.getRemarks());
    }
}
