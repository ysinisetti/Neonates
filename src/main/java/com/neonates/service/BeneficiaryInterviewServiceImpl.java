package com.neonates.service;

import com.neonates.Enum.InterviewStatus;
import com.neonates.entity.BeneficiaryInterview;
import com.neonates.entity.Case;
import com.neonates.exception.ResourceNotFoundException;
import com.neonates.mapper.BeneficiaryInterviewMapper;
import com.neonates.repository.BeneficiaryInterviewRepository;
import com.neonates.repository.CaseRepository;
import com.neonates.request.BeneficiaryInterviewCreateRequest;
import com.neonates.response.BeneficiaryInterviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BeneficiaryInterviewServiceImpl implements BeneficiaryInterviewService {

    private final BeneficiaryInterviewRepository interviewRepository;
    private final CaseRepository caseRepository;
    private final BeneficiaryInterviewMapper mapper;

    @Override
    @Transactional
    public BeneficiaryInterviewResponse createInterview(BeneficiaryInterviewCreateRequest request) {
        // Validate case exists
        Case caseMaster = caseRepository.findById(request.getCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Case not found with id: " + request.getCaseId()));

        // Check if interview already exists for the case
        if (interviewRepository.findByCaseMaster_CaseId(request.getCaseId()).isPresent()) {
            throw new IllegalArgumentException("Interview already exists for this case");
        }

        BeneficiaryInterview interview = mapper.toEntity(request);
        interview.setCaseMaster(caseMaster);

        // Set default status if not provided
        if (interview.getInterviewStatus() == null) {
            interview.setInterviewStatus(InterviewStatus.Not_Started);
        }

        // Business logic: if status is Draft or Completed, set accordingly
        if (interview.getInterviewStatus() == InterviewStatus.Completed) {
            if (interview.getInterviewedBy() == null) {
                throw new IllegalArgumentException("Interviewed by is required when status is Completed");
            }
            if (interview.getOutcome() == null) {
                throw new IllegalArgumentException("Outcome is required when status is Completed");
            }
            interview.setInterviewedAt(LocalDateTime.now());
        }

        interview = interviewRepository.save(interview);
        return mapper.toResponse(interview);
    }

    @Override
    @Transactional
    public BeneficiaryInterviewResponse updateInterview(Long id, BeneficiaryInterviewCreateRequest request) {
        BeneficiaryInterview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found with id: " + id));

        // Validate status transitions
        InterviewStatus newStatus = request.getInterviewStatus() != null ? request.getInterviewStatus() : interview.getInterviewStatus();
        validateStatusTransition(interview.getInterviewStatus(), newStatus);

        mapper.updateEntityFromRequest(request, interview);

        // Business logic
        if (newStatus == InterviewStatus.Completed) {
            if (interview.getInterviewedBy() == null) {
                throw new IllegalArgumentException("Interviewed by is required when status is Completed");
            }
            if (interview.getOutcome() == null) {
                throw new IllegalArgumentException("Outcome is required when status is Completed");
            }
            interview.setInterviewedAt(LocalDateTime.now());
        }

        interview = interviewRepository.save(interview);
        return mapper.toResponse(interview);
    }

    @Override
    public BeneficiaryInterviewResponse getInterviewByCaseId(Long caseId) {
        BeneficiaryInterview interview = interviewRepository.findByCaseMaster_CaseId(caseId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found for case id: " + caseId));
        return mapper.toResponse(interview);
    }

    private void validateStatusTransition(InterviewStatus current, InterviewStatus newStatus) {
        if (current == InterviewStatus.Completed && newStatus != InterviewStatus.Completed) {
            throw new IllegalArgumentException("Cannot change status from Completed");
        }
        // Add more validations if needed
    }
}
