package com.neonates.controller;

import com.neonates.request.BeneficiaryInterviewCreateRequest;
import com.neonates.response.ApiResponse;
import com.neonates.response.BeneficiaryInterviewResponse;
import com.neonates.service.BeneficiaryInterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class BeneficiaryInterviewController {

    private final BeneficiaryInterviewService service;

    @PostMapping
    public ResponseEntity<ApiResponse<BeneficiaryInterviewResponse>> createInterview(@RequestBody BeneficiaryInterviewCreateRequest request) {
        BeneficiaryInterviewResponse data = service.createInterview(request);
        return new ResponseEntity<>(ApiResponse.success("Interview created successfully", data, HttpStatus.CREATED.value(), true), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BeneficiaryInterviewResponse>> updateInterview(@PathVariable Long id, @RequestBody BeneficiaryInterviewCreateRequest request) {
        BeneficiaryInterviewResponse data = service.updateInterview(id, request);
        return ResponseEntity.ok(ApiResponse.success("Interview updated successfully", data, HttpStatus.OK.value(), true));
    }

    @GetMapping("/case/{caseId}")
    public ResponseEntity<ApiResponse<BeneficiaryInterviewResponse>> getInterviewByCaseId(@PathVariable Long caseId) {
        BeneficiaryInterviewResponse data = service.getInterviewByCaseId(caseId);
        return ResponseEntity.ok(ApiResponse.success("Interview fetched successfully", data, HttpStatus.OK.value(), true));
    }
}
