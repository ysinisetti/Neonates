package com.neonates.controller;

import com.neonates.request.ClinicalCaseCreateRequest;
import com.neonates.response.ApiResponse;
import com.neonates.response.ClinicalCaseResponse;
import com.neonates.service.ClinicalCaseDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/clinical-case-details")
@RequiredArgsConstructor
@CrossOrigin
public class ClinicalCaseDetailsController {

    private final ClinicalCaseDetailsService clinicalCaseDetailsService;

    // POST /api/clinical-details
    @PostMapping
    public ResponseEntity<ApiResponse<ClinicalCaseResponse>> createClinicalCaseDetails(@Valid @RequestBody ClinicalCaseCreateRequest request) {
        ClinicalCaseResponse data = clinicalCaseDetailsService.createClinicalCaseDetails(request);
        return new ResponseEntity<>(ApiResponse.success("Clinical case details created successfully", data, HttpStatus.CREATED.value(), true), HttpStatus.CREATED);
    }

    // GET /api/clinical-details/case/{caseId}
    @GetMapping("/case/{caseId}")
    public ResponseEntity<ApiResponse<ClinicalCaseResponse>> getClinicalDetailsByCaseId(@PathVariable Long caseId) {
        Optional<ClinicalCaseResponse> optionalData = clinicalCaseDetailsService.getClinicalDetailsByCaseId(caseId);
        if (optionalData.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("Clinical details fetched successfully", optionalData.get(), HttpStatus.OK.value(), true));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.success("Clinical details not found for the given case ID", null, HttpStatus.NOT_FOUND.value(), false));
        }
    }

    // PUT /api/clinical-details/{clinicalId}
    @PutMapping("/{clinicalId}")
    public ResponseEntity<ApiResponse<ClinicalCaseResponse>> updateClinicalCaseDetails(@PathVariable Long clinicalId, @Valid @RequestBody ClinicalCaseCreateRequest request) {
        ClinicalCaseResponse data = clinicalCaseDetailsService.updateClinicalCaseDetails(clinicalId, request);
        return ResponseEntity.ok(ApiResponse.success("Clinical case details updated successfully", data, HttpStatus.OK.value(), true));
    }
}
