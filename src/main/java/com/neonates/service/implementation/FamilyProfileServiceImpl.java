package com.neonates.service.implementation;

import com.neonates.entity.FamilyProfile;
import com.neonates.exception.ResourceNotFoundException;
import com.neonates.mapper.FamilyProfileMapper;
import com.neonates.repository.FamilyProfileRepository;
import com.neonates.request.FamilyProfileRequest;
import com.neonates.response.FamilyProfileResponse;
import com.neonates.service.FamilyProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FamilyProfileServiceImpl implements FamilyProfileService {

    private final FamilyProfileRepository familyProfileRepository;
    private final FamilyProfileMapper familyProfileMapper;

    @Override
    public FamilyProfileResponse createFamilyProfile(FamilyProfileRequest request) {
        // Check if family profile already exists for the case
        if (familyProfileRepository.findByCaseId(request.getCaseId()).isPresent()) {
            throw new IllegalArgumentException("Family profile already exists for case ID: " + request.getCaseId());
        }

        // Map to entity
        FamilyProfile entity = familyProfileMapper.toEntity(request);

        // Set default values for boolean flags if not provided
        if (entity.getIncomeThresholdFlag() == null) {
            entity.setIncomeThresholdFlag(false);
        }
        if (entity.getIncomeExceptionReviewRequired() == null) {
            entity.setIncomeExceptionReviewRequired(false);
        }

        // Calculate total family income if not provided
        if (entity.getTotalFamilyIncome() == null) {
            entity.setTotalFamilyIncome(calculateTotalIncome(entity));
        }

        // Set threshold flag (example logic: if total > 50000, flag = true)
        entity.setIncomeThresholdFlag(entity.getTotalFamilyIncome() != null && entity.getTotalFamilyIncome().compareTo(BigDecimal.valueOf(50000)) > 0);

        // Save and return
        FamilyProfile saved = familyProfileRepository.save(entity);
        return familyProfileMapper.toResponse(saved);
    }

    @Override
    public Optional<FamilyProfileResponse> getFamilyProfileByCaseId(Long caseId) {
        return familyProfileRepository.findByCaseId(caseId)
                .map(familyProfileMapper::toResponse);
    }

    @Override
    public FamilyProfileResponse updateFamilyProfile(Long familyId, FamilyProfileRequest request) {
        FamilyProfile entity = familyProfileRepository.findById(familyId)
                .orElseThrow(() -> new ResourceNotFoundException("Family profile not found with id: " + familyId));

        // Update entity
        familyProfileMapper.updateEntityFromRequest(request, entity);

        // Set default values for boolean flags if null
        if (entity.getIncomeThresholdFlag() == null) {
            entity.setIncomeThresholdFlag(false);
        }
        if (entity.getIncomeExceptionReviewRequired() == null) {
            entity.setIncomeExceptionReviewRequired(false);
        }

        // Recalculate total income if incomes changed
        entity.setTotalFamilyIncome(calculateTotalIncome(entity));

        // Update threshold flag
        entity.setIncomeThresholdFlag(entity.getTotalFamilyIncome() != null && entity.getTotalFamilyIncome().compareTo(BigDecimal.valueOf(50000)) > 0);

        // Save and return
        FamilyProfile updated = familyProfileRepository.save(entity);
        return familyProfileMapper.toResponse(updated);
    }

    private BigDecimal calculateTotalIncome(FamilyProfile entity) {
        BigDecimal total = BigDecimal.ZERO;
        if (entity.getIncomeCaptureBasis() != null) {
            switch (entity.getIncomeCaptureBasis()) {
                case Monthly:
                    if (entity.getFatherMonthlyIncome() != null) total = total.add(entity.getFatherMonthlyIncome());
                    if (entity.getMotherMonthlyIncome() != null) total = total.add(entity.getMotherMonthlyIncome());
                    break;
                case Daily_Wage:
                    BigDecimal dailyTotal = BigDecimal.ZERO;
                    if (entity.getFatherDailyWage() != null) dailyTotal = dailyTotal.add(entity.getFatherDailyWage());
                    if (entity.getMotherDailyWage() != null) dailyTotal = dailyTotal.add(entity.getMotherDailyWage());
                    total = dailyTotal.multiply(BigDecimal.valueOf(30));
                    break;
                case Mixed:
                    if (entity.getFatherMonthlyIncome() != null) total = total.add(entity.getFatherMonthlyIncome());
                    if (entity.getMotherMonthlyIncome() != null) total = total.add(entity.getMotherMonthlyIncome());
                    BigDecimal dailyMixed = BigDecimal.ZERO;
                    if (entity.getFatherDailyWage() != null) dailyMixed = dailyMixed.add(entity.getFatherDailyWage());
                    if (entity.getMotherDailyWage() != null) dailyMixed = dailyMixed.add(entity.getMotherDailyWage());
                    total = total.add(dailyMixed.multiply(BigDecimal.valueOf(30)));
                    break;
                case Unknown:
                default:
                    total = BigDecimal.ZERO;
                    break;
            }
        }
        return total;
    }
}
