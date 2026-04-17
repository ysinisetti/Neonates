package com.neonates.controller;

import com.neonates.entity.ClinicalDetails;
import com.neonates.request.ClinicalDetailsRequest;
import com.neonates.response.ApiResponse;
import com.neonates.service.ClinicalDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/clinical-details")
@RequiredArgsConstructor
@CrossOrigin
public class ClinicalDetailsController {

    private final ClinicalDetailsService service;

    @PostMapping
    public ResponseEntity<ApiResponse<ClinicalDetails>> saveOrUpdateClinicalDetails(@RequestBody ClinicalDetailsRequest request) {
        try {
            ClinicalDetails savedDetails = service.saveOrUpdateClinicalDetails(request);
            return ResponseEntity.ok(ApiResponse.success("Clinical details saved successfully", savedDetails, HttpStatus.OK.value(), true));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.success(e.getMessage(), null, HttpStatus.BAD_REQUEST.value(), false));
        }
    }

    @GetMapping("/case/{caseId}")
    public ResponseEntity<ApiResponse<ClinicalDetails>> getClinicalDetailsByCaseId(@PathVariable Long caseId) {
        Optional<ClinicalDetails> details = service.getByCaseId(caseId);
        if (details.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("Clinical details fetched successfully", details.get(), HttpStatus.OK.value(), true));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.success("Clinical details not found", null, HttpStatus.NOT_FOUND.value(), false));
        }
    }

    @PutMapping("/{clinicalId}")
    public ResponseEntity<ApiResponse<ClinicalDetails>> updateClinicalDetails(@PathVariable Long clinicalId, @RequestBody ClinicalDetailsRequest request) {
        try {
            ClinicalDetails updatedDetails = service.updateClinicalDetails(clinicalId, request);
            return ResponseEntity.ok(ApiResponse.success("Clinical details updated successfully", updatedDetails, HttpStatus.OK.value(), true));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.success(e.getMessage(), null, HttpStatus.NOT_FOUND.value(), false));
        }
    }
}
