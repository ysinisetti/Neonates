package com.neonates.service;

import com.neonates.request.PanelAssignmentCreateRequest;
import com.neonates.request.PanelAssignmentStatusUpdateRequest;
import com.neonates.response.PanelAssignmentResponse;

import java.util.List;

public interface PanelAssignmentService {

    PanelAssignmentResponse createPanelAssignment(PanelAssignmentCreateRequest request);

    List<PanelAssignmentResponse> getPanelAssignmentsByCaseId(Long caseId);

    List<PanelAssignmentResponse> getPanelAssignmentsByReviewerUserId(Long reviewerUserId);

    PanelAssignmentResponse updatePanelAssignmentStatus(Long panelAssignmentId, PanelAssignmentStatusUpdateRequest request);
}
