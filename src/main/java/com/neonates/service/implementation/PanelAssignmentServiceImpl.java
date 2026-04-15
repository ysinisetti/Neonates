package com.neonates.service.implementation;

import com.neonates.Enum.AssignmentStatus;
import com.neonates.entity.Case;
import com.neonates.entity.PanelAssignment;
import com.neonates.exception.ResourceNotFoundException;
import com.neonates.mapper.PanelAssignmentMapper;
import com.neonates.repository.CaseRepository;
import com.neonates.repository.PanelAssignmentRepository;
import com.neonates.request.PanelAssignmentCreateRequest;
import com.neonates.request.PanelAssignmentStatusUpdateRequest;
import com.neonates.response.PanelAssignmentResponse;
import com.neonates.service.PanelAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PanelAssignmentServiceImpl implements PanelAssignmentService {

    private final PanelAssignmentRepository panelAssignmentRepository;
    private final CaseRepository caseRepository;
    private final PanelAssignmentMapper panelAssignmentMapper;

    @Override
    public PanelAssignmentResponse createPanelAssignment(PanelAssignmentCreateRequest request) {
        // Validate case exists
        caseRepository.findById(request.getCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));

        // Check for existing assignment for this case and panel type
        List<PanelAssignment> existingAssignments = panelAssignmentRepository.findByCaseId(request.getCaseId())
                .stream()
                .filter(pa -> pa.getPanelType().equals(request.getPanelType()))
                .collect(Collectors.toList());

        if (!existingAssignments.isEmpty()) {
            // Reassign: update existing assignment
            PanelAssignment existing = existingAssignments.get(0);
            existing.setAssignmentStatus(AssignmentStatus.Reassigned);
            existing.setReviewerUserId(request.getReviewerUserId());
            existing.setAssignedBy(request.getAssignedBy());
            existing.setNotes(request.getNotes());
            existing.setDueDate(request.getDeadline());
            existing.setUpdatedAt(java.time.LocalDateTime.now());
            panelAssignmentRepository.save(existing);
            return panelAssignmentMapper.toResponse(existing);
        } else {
            // Create new assignment
            PanelAssignment entity = panelAssignmentMapper.toEntity(request);
            PanelAssignment saved = panelAssignmentRepository.save(entity);
            return panelAssignmentMapper.toResponse(saved);
        }
    }

    @Override
    public List<PanelAssignmentResponse> getPanelAssignmentsByCaseId(Long caseId) {
        List<PanelAssignment> assignments = panelAssignmentRepository.findByCaseId(caseId);
        return assignments.stream()
                .map(panelAssignmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PanelAssignmentResponse> getPanelAssignmentsByReviewerUserId(Long reviewerUserId) {
        List<PanelAssignment> assignments = panelAssignmentRepository.findByReviewerUserId(reviewerUserId);
        return assignments.stream()
                .map(panelAssignmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PanelAssignmentResponse updatePanelAssignmentStatus(Long panelAssignmentId, PanelAssignmentStatusUpdateRequest request) {
        PanelAssignment assignment = panelAssignmentRepository.findById(panelAssignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Panel assignment not found"));

        assignment.setAssignmentStatus(request.getAssignmentStatus());
        if (request.getNotes() != null) {
            assignment.setNotes(request.getNotes());
        }
        assignment.setUpdatedAt(java.time.LocalDateTime.now());

        PanelAssignment saved = panelAssignmentRepository.save(assignment);
        return panelAssignmentMapper.toResponse(saved);
    }
}
