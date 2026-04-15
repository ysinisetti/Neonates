package com.neonates.controller;

import com.neonates.request.ChildProfileRequest;
import com.neonates.response.ApiResponse;
import com.neonates.response.ChildProfileResponse;
import com.neonates.service.ChildProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/child-profiles")
@RequiredArgsConstructor
@CrossOrigin
public class ChildProfileController {

    private final ChildProfileService childProfileService;

    // POST /api/child-profiles
    @PostMapping
    public ResponseEntity<ApiResponse<ChildProfileResponse>> createChildProfile(@Valid @RequestBody ChildProfileRequest request) {
        ChildProfileResponse data = childProfileService.createChildProfile(request);
        return new ResponseEntity<>(ApiResponse.success("Child profile created successfully", data, HttpStatus.CREATED.value(), true), HttpStatus.CREATED);
    }

    // GET /api/child-profiles/case/{caseId}
    @GetMapping("/case/{caseId}")
    public ResponseEntity<ApiResponse<List<ChildProfileResponse>>> getChildProfilesByCaseId(@PathVariable Long caseId) {
        List<ChildProfileResponse> data = childProfileService.getChildProfilesByCaseId(caseId);
        return ResponseEntity.ok(ApiResponse.success("Child profiles fetched successfully", data, HttpStatus.OK.value(), true));
    }

    // PUT /api/child-profiles/{childId}
    @PutMapping("/{childId}")
    public ResponseEntity<ApiResponse<ChildProfileResponse>> updateChildProfile(@PathVariable Long childId, @Valid @RequestBody ChildProfileRequest request) {
        ChildProfileResponse data = childProfileService.updateChildProfile(childId, request);
        return ResponseEntity.ok(ApiResponse.success("Child profile updated successfully", data, HttpStatus.OK.value(), true));
    }
}
