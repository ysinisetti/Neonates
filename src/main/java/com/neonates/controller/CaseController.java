package com.neonates.controller;

import com.neonates.request.CaseCreateRequestDTO;
import com.neonates.response.ApiResponse;
import com.neonates.response.CaseResponseDTO;
import com.neonates.service.CaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cases")
@RequiredArgsConstructor
@CrossOrigin
public class CaseController {

    private final CaseService caseService;

    // POST /api/cases
    @PostMapping
    public ResponseEntity<ApiResponse<CaseResponseDTO>> createCase(@Valid @RequestBody CaseCreateRequestDTO request) {
        CaseResponseDTO data = caseService.createCase(request);
        return new ResponseEntity<>(ApiResponse.success("Case created successfully", data, HttpStatus.CREATED.value(), true), HttpStatus.CREATED);
    }

    // GET /api/cases/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CaseResponseDTO>> getCaseById(@PathVariable Long id) {
        CaseResponseDTO data = caseService.getCaseById(id);
        return ResponseEntity.ok(ApiResponse.success("Case fetched successfully", data, HttpStatus.OK.value(), true));
    }

    // DELETE /api/cases/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCase(@PathVariable Long id) {
        caseService.deleteCase(id);
        return ResponseEntity.ok(ApiResponse.success("Case deleted successfully", null, HttpStatus.OK.value(), true));
    }
}
