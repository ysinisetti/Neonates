package com.neonates.controller;

import com.neonates.request.HospitalRequestDTO;
import com.neonates.response.ApiResponse;
import com.neonates.response.HospitalResponseDTO;
import com.neonates.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    // POST /api/hospitals
    @PostMapping
    public ResponseEntity<ApiResponse<HospitalResponseDTO>> createHospital(@RequestBody HospitalRequestDTO request) {
        HospitalResponseDTO data = hospitalService.createHospital(request);
        return new ResponseEntity<>(ApiResponse.success("Hospital created successfully", data, HttpStatus.CREATED.value(), true), HttpStatus.CREATED);
    }

    // PUT /api/hospitals/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HospitalResponseDTO>> updateHospital(@PathVariable Long id, @RequestBody HospitalRequestDTO request) {
        HospitalResponseDTO data = hospitalService.updateHospital(id, request);
        return ResponseEntity.ok(ApiResponse.success("Hospital updated successfully", data, HttpStatus.OK.value(), true));
    }

    // GET /api/hospitals
    @GetMapping
    public ResponseEntity<ApiResponse<List<HospitalResponseDTO>>> getAllHospitals() {
        List<HospitalResponseDTO> data = hospitalService.getAllHospitals();
        return ResponseEntity.ok(ApiResponse.success("Hospitals fetched successfully", data, HttpStatus.OK.value(), true));
    }

    // GET /api/hospitals/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HospitalResponseDTO>> getHospitalById(@PathVariable Long id) {
        HospitalResponseDTO data = hospitalService.getHospitalById(id);
        return ResponseEntity.ok(ApiResponse.success("Hospital fetched successfully", data, HttpStatus.OK.value(), true));
    }

    // DELETE /api/hospitals/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivateHospital(@PathVariable Long id) {
        hospitalService.deactivateHospital(id);
        return ResponseEntity.ok(ApiResponse.success("Hospital deactivated successfully", null, HttpStatus.OK.value(), true));
    }
}
