package com.neonates.controller;

import com.neonates.request.FinancialCaseCreateRequest;
import com.neonates.response.ApiResponse;
import com.neonates.response.FinancialCaseResponse;
import com.neonates.service.FinancialCaseDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/financial-details")
@RequiredArgsConstructor
@CrossOrigin
public class FinancialCaseDetailsController {

    private final FinancialCaseDetailsService financialCaseDetailsService;

    // POST /api/financial-details
    @PostMapping
    public ResponseEntity<ApiResponse<FinancialCaseResponse>> createFinancialCaseDetails(@Valid @RequestBody FinancialCaseCreateRequest request) {
        FinancialCaseResponse data = financialCaseDetailsService.createFinancialCaseDetails(request);
        return new ResponseEntity<>(ApiResponse.success("Financial case details created successfully", data, HttpStatus.CREATED.value(), true), HttpStatus.CREATED);
    }

    // GET /api/financial-details/case/{caseId}
    @GetMapping("/case/{caseId}")
    public ResponseEntity<ApiResponse<FinancialCaseResponse>> getFinancialDetailsByCaseId(@PathVariable Long caseId) {
        Optional<FinancialCaseResponse> optionalData = financialCaseDetailsService.getFinancialDetailsByCaseId(caseId);
        if (optionalData.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("Financial details fetched successfully", optionalData.get(), HttpStatus.OK.value(), true));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.success("Financial details not found for the given case ID", null, HttpStatus.NOT_FOUND.value(), false));
        }
    }

    // PUT /api/financial-details/{financialId}
    @PutMapping("/{financialId}")
    public ResponseEntity<ApiResponse<FinancialCaseResponse>> updateFinancialCaseDetails(@PathVariable Long financialId, @Valid @RequestBody FinancialCaseCreateRequest request) {
        FinancialCaseResponse data = financialCaseDetailsService.updateFinancialCaseDetails(financialId, request);
        return ResponseEntity.ok(ApiResponse.success("Financial case details updated successfully", data, HttpStatus.OK.value(), true));
    }
}
