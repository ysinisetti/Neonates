package com.neonates.controller;

import com.neonates.request.PanelAssignmentCreateRequest;
import com.neonates.request.PanelAssignmentStatusUpdateRequest;
import com.neonates.response.ApiResponse;
import com.neonates.response.PanelAssignmentResponse;
import com.neonates.service.PanelAssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/panel-assignments")
@RequiredArgsConstructor
@CrossOrigin
public class PanelAssignmentController {

    private final PanelAssignmentService panelAssignmentService;

    // POST /api/panel-assignments
    @PostMapping
    public ResponseEntity<ApiResponse<PanelAssignmentResponse>> createPanelAssignment(@Valid @RequestBody PanelAssignmentCreateRequest request) {
        PanelAssignmentResponse data = panelAssignmentService.createPanelAssignment(request);
        return new ResponseEntity<>(ApiResponse.success("Panel assignment created successfully", data, HttpStatus.CREATED.value(), true), HttpStatus.CREATED);
    }

    // GET /api/panel-assignments/case/{caseId}
    @GetMapping("/case/{caseId}")
    public ResponseEntity<ApiResponse<List<PanelAssignmentResponse>>> getPanelAssignmentsByCaseId(@PathVariable Long caseId) {
        List<PanelAssignmentResponse> data = panelAssignmentService.getPanelAssignmentsByCaseId(caseId);
        return ResponseEntity.ok(ApiResponse.success("Panel assignments fetched successfully", data, HttpStatus.OK.value(), true));
    }

    // GET /api/panel-assignments/reviewer/{userId}
    @GetMapping("/reviewer/{userId}")
    public ResponseEntity<ApiResponse<List<PanelAssignmentResponse>>> getPanelAssignmentsByReviewerUserId(@PathVariable Long userId) {
        List<PanelAssignmentResponse> data = panelAssignmentService.getPanelAssignmentsByReviewerUserId(userId);
        return ResponseEntity.ok(ApiResponse.success("Panel assignments fetched successfully", data, HttpStatus.OK.value(), true));
    }

    // PUT /api/panel-assignments/{id}/status
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<PanelAssignmentResponse>> updatePanelAssignmentStatus(@PathVariable Long id, @Valid @RequestBody PanelAssignmentStatusUpdateRequest request) {
        PanelAssignmentResponse data = panelAssignmentService.updatePanelAssignmentStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success("Panel assignment status updated successfully", data, HttpStatus.OK.value(), true));
    }
}
