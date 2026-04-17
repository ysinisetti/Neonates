package com.neonates.service;

import com.neonates.entity.ClinicalDetails;
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

    @Transactional
    public ClinicalDetails saveOrUpdateClinicalDetails(ClinicalDetailsRequest request) {
        if (request.getCaseId() == null) {
            throw new IllegalArgumentException("CaseId is mandatory");
        }

        Optional<ClinicalDetails> existingOpt = repository.findByCaseId(request.getCaseId());
        ClinicalDetails details;

        if (existingOpt.isPresent()) {
            details = existingOpt.get();
        } else {
            details = new ClinicalDetails();
            details.setCaseId(request.getCaseId());
        }

        // Mapping fields
        details.setAntenatalRiskFactors(request.getAntenatalRiskFactors());
        details.setRiskNotes(request.getRiskNotes());
        details.setDiagnoses(request.getDiagnoses());
        details.setOtherDiagnosis(request.getOtherDiagnosis());
        details.setRespirationSupport(request.getRespirationSupport());
        
        details.setIvAntibiotics(request.getIvAntibiotics() != null ? request.getIvAntibiotics() : false);
        details.setIonotropes(request.getIonotropes() != null ? request.getIonotropes() : false);
        details.setTpn(request.getTpn() != null ? request.getTpn() : false);
        
        details.setOtherTreatment(request.getOtherTreatment());
        details.setCurrentDayOfLife(request.getCurrentDayOfLife());
        details.setCurrentWeightKg(request.getCurrentWeightKg());
        details.setCorrectedGestationalAgeWeeks(request.getCorrectedGestationalAgeWeeks());
        
        details.setMechanicalVentilation(request.getMechanicalVentilation() != null ? request.getMechanicalVentilation() : false);
        details.setCpap(request.getCpap() != null ? request.getCpap() : false);
        details.setHfnc(request.getHfnc() != null ? request.getHfnc() : false);
        details.setOxygenSupport(request.getOxygenSupport() != null ? request.getOxygenSupport() : false);
        
        details.setNpo(request.getNpo() != null ? request.getNpo() : false);
        details.setOgFeeding(request.getOgFeeding() != null ? request.getOgFeeding() : false);
        details.setPaladaFeeding(request.getPaladaFeeding() != null ? request.getPaladaFeeding() : false);
        details.setDbfFeeding(request.getDbfFeeding() != null ? request.getDbfFeeding() : false);
        
        details.setOtherFeeding(request.getOtherFeeding());
        details.setDischargePlan(request.getDischargePlan());
        
        details.setLabsAttached(request.getLabsAttached() != null ? request.getLabsAttached() : false);
        details.setXrayAttached(request.getXrayAttached() != null ? request.getXrayAttached() : false);
        details.setScansAttached(request.getScansAttached() != null ? request.getScansAttached() : false);
        details.setOtherReportsAttached(request.getOtherReportsAttached() != null ? request.getOtherReportsAttached() : false);
        
        details.setOtherInvestigationDetails(request.getOtherInvestigationDetails());
        details.setRemarks(request.getRemarks());
        details.setSignatureFilePath(request.getSignatureFilePath());
        details.setSignedDate(request.getSignedDate());

        return repository.save(details);
    }

    public Optional<ClinicalDetails> getByCaseId(Long caseId) {
        return repository.findByCaseId(caseId);
    }
}
