package com.neonates.controller;

import com.neonates.request.DonorRequest;
import com.neonates.response.ApiResponse;
import com.neonates.response.DonorResponse;
import com.neonates.service.DonorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donors")
@RequiredArgsConstructor
@CrossOrigin
public class DonorController {

    private final DonorService donorService;

    // POST /api/donors
    @PostMapping
    public ResponseEntity<ApiResponse<DonorResponse>> createDonor(@RequestBody DonorRequest request) {
        DonorResponse data = donorService.createDonor(request);
        return new ResponseEntity<>(ApiResponse.success("Donor created successfully", data, HttpStatus.CREATED.value(), true), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DonorResponse>> getDonorById(@PathVariable Long id) {
        DonorResponse data = donorService.getDonorById(id);
        return ResponseEntity.ok(ApiResponse.success("Donor fetched successfully", data, HttpStatus.OK.value(), true));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DonorResponse>>> getAllDonors() {
        List<DonorResponse> data = donorService.getAllDonors();
        return ResponseEntity.ok(ApiResponse.success("Donors fetched successfully", data, HttpStatus.OK.value(), true));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DonorResponse>> updateDonor(@PathVariable Long id, @RequestBody DonorRequest request) {
        DonorResponse data = donorService.updateDonor(id, request);
        return ResponseEntity.ok(ApiResponse.success("Donor updated successfully", data, HttpStatus.OK.value(), true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDonor(@PathVariable Long id) {
        donorService.deleteDonor(id);
        return ResponseEntity.ok(ApiResponse.success("Donor deleted successfully", null, HttpStatus.OK.value(), true));
    }
}
