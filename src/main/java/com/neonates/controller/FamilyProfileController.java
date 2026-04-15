package com.neonates.controller;

import com.neonates.request.FamilyProfileRequest;
import com.neonates.response.ApiResponse;
import com.neonates.response.FamilyProfileResponse;
import com.neonates.service.FamilyProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/family-profiles")
@RequiredArgsConstructor
@CrossOrigin
public class FamilyProfileController {

    private final FamilyProfileService familyProfileService;

    // POST /api/family-profiles
    @PostMapping
    public ResponseEntity<ApiResponse<FamilyProfileResponse>> createFamilyProfile(@Valid @RequestBody FamilyProfileRequest request) {
        FamilyProfileResponse data = familyProfileService.createFamilyProfile(request);
        return new ResponseEntity<>(ApiResponse.success("Family profile created successfully", data, HttpStatus.CREATED.value(), true), HttpStatus.CREATED);
    }

    // GET /api/family-profiles/case/{caseId}
    @GetMapping("/case/{caseId}")
    public ResponseEntity<ApiResponse<FamilyProfileResponse>> getFamilyProfileByCaseId(@PathVariable Long caseId) {
        Optional<FamilyProfileResponse> optionalData = familyProfileService.getFamilyProfileByCaseId(caseId);
        if (optionalData.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("Family profile fetched successfully", optionalData.get(), HttpStatus.OK.value(), true));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.success("Family profile not found for the given case ID", null, HttpStatus.NOT_FOUND.value(), false));
        }
    }

    // PUT /api/family-profiles/{familyId}
    @PutMapping("/{familyId}")
    public ResponseEntity<ApiResponse<FamilyProfileResponse>> updateFamilyProfile(@PathVariable Long familyId, @Valid @RequestBody FamilyProfileRequest request) {
        FamilyProfileResponse data = familyProfileService.updateFamilyProfile(familyId, request);
        return ResponseEntity.ok(ApiResponse.success("Family profile updated successfully", data, HttpStatus.OK.value(), true));
    }
}
