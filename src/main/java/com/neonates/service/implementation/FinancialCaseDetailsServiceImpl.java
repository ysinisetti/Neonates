package com.neonates.service.implementation;

import com.neonates.entity.FinancialCaseDetails;
import com.neonates.exception.ResourceNotFoundException;
import com.neonates.mapper.FinancialCaseDetailsMapper;
import com.neonates.repository.CaseRepository;
import com.neonates.repository.FinancialCaseDetailsRepository;
import com.neonates.request.FinancialCaseCreateRequest;
import com.neonates.response.FinancialCaseResponse;
import com.neonates.service.FinancialCaseDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinancialCaseDetailsServiceImpl implements FinancialCaseDetailsService {

    private final FinancialCaseDetailsRepository financialCaseDetailsRepository;
    private final FinancialCaseDetailsMapper financialCaseDetailsMapper;
    private final CaseRepository caseRepository;

    @Override
    public FinancialCaseResponse createFinancialCaseDetails(FinancialCaseCreateRequest request) {
        log.info("Creating financial case details for case ID: {}", request.getCaseId());

        // Validate case_id exists
        caseRepository.findById(request.getCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Case not found with id: " + request.getCaseId()));

        // Map to entity
        FinancialCaseDetails entity = financialCaseDetailsMapper.toEntity(request);

        // Save (calculations will happen in @PrePersist)
        FinancialCaseDetails saved = financialCaseDetailsRepository.save(entity);
        log.info("Financial case details created with ID: {}", saved.getFinancialId());

        return financialCaseDetailsMapper.toResponse(saved);
    }

    @Override
    public Optional<FinancialCaseResponse> getFinancialDetailsByCaseId(Long caseId) {
        log.info("Fetching financial details for case ID: {}", caseId);
        return financialCaseDetailsRepository.findByCaseId(caseId)
                .map(financialCaseDetailsMapper::toResponse);
    }

    @Override
    public FinancialCaseResponse updateFinancialCaseDetails(Long financialId, FinancialCaseCreateRequest request) {
        log.info("Updating financial case details with ID: {}", financialId);

        // Validate financial details exist
        FinancialCaseDetails entity = financialCaseDetailsRepository.findById(financialId)
                .orElseThrow(() -> new ResourceNotFoundException("Financial case details not found with id: " + financialId));

        // Validate case_id exists
        caseRepository.findById(request.getCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Case not found with id: " + request.getCaseId()));

        // Update fields
        updateEntityFromRequest(request, entity);

        // Save (calculations will happen in @PreUpdate)
        FinancialCaseDetails updated = financialCaseDetailsRepository.save(entity);
        log.info("Financial case details updated with ID: {}", updated.getFinancialId());

        return financialCaseDetailsMapper.toResponse(updated);
    }

    private void updateEntityFromRequest(FinancialCaseCreateRequest request, FinancialCaseDetails entity) {
        entity.setCaseId(request.getCaseId());
        entity.setIncomeProofType(request.getIncomeProofType());
        entity.setBankStatementMonthsCount(request.getBankStatementMonthsCount());
        entity.setEconomicCardType(request.getEconomicCardType());
        entity.setBplAplStatus(request.getBplAplStatus());
        entity.setAdvancePaidAmount(request.getAdvancePaidAmount());
        entity.setMedicalBillEstimate(request.getMedicalBillEstimate());
        entity.setEstimatedNicuDays(request.getEstimatedNicuDays());
        entity.setHospitalDiscountAmount(request.getHospitalDiscountAmount());
        entity.setReductionAmount(request.getReductionAmount());
        entity.setReductionNotes(request.getReductionNotes());
        entity.setSponsorAmountQuantified(request.getSponsorAmountQuantified());
        entity.setSponsorAmountFinal(request.getSponsorAmountFinal());
        entity.setFinancialSummarySnapshot(request.getFinancialSummarySnapshot());
        entity.setManualExceptionNotes(request.getManualExceptionNotes());
    }
}
