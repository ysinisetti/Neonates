package com.neonates.controller;

import com.neonates.request.HospitalProcessMapRequestDTO;
import com.neonates.response.ApiResponse;
import com.neonates.response.HospitalProcessMapResponseDTO;
import com.neonates.service.HospitalProcessMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospital-process-maps")
@RequiredArgsConstructor
@CrossOrigin
public class HospitalProcessMapController {

    private final HospitalProcessMapService hospitalProcessMapService;

    // POST /api/hospital-process-maps
    @PostMapping
    public ResponseEntity<ApiResponse<HospitalProcessMapResponseDTO>> createMapping(@RequestBody HospitalProcessMapRequestDTO request) {
        HospitalProcessMapResponseDTO data = hospitalProcessMapService.createMapping(request);
        return new ResponseEntity<>(ApiResponse.success("Hospital process mapping created successfully", data, HttpStatus.CREATED.value(), true), HttpStatus.CREATED);
    }

    // PUT /api/hospital-process-maps/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HospitalProcessMapResponseDTO>> updateMapping(@PathVariable Long id, @RequestBody HospitalProcessMapRequestDTO request) {
        HospitalProcessMapResponseDTO data = hospitalProcessMapService.updateMapping(id, request);
        return ResponseEntity.ok(ApiResponse.success("Hospital process mapping updated successfully", data, HttpStatus.OK.value(), true));
    }

    // GET /api/hospital-process-maps/hospital/{hospitalId}
    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<ApiResponse<List<HospitalProcessMapResponseDTO>>> getMappingsByHospital(@PathVariable Long hospitalId) {
        List<HospitalProcessMapResponseDTO> data = hospitalProcessMapService.getMappingsByHospital(hospitalId);
        return ResponseEntity.ok(ApiResponse.success("Hospital process mappings fetched successfully", data, HttpStatus.OK.value(), true));
    }

    // GET /api/hospital-process-maps/active
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<HospitalProcessMapResponseDTO>>> getActiveMappings() {
        List<HospitalProcessMapResponseDTO> data = hospitalProcessMapService.getActiveMappings();
        return ResponseEntity.ok(ApiResponse.success("Active hospital process mappings fetched successfully", data, HttpStatus.OK.value(), true));
    }

    // DELETE /api/hospital-process-maps/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivateMapping(@PathVariable Long id) {
        hospitalProcessMapService.deactivateMapping(id);
        return ResponseEntity.ok(ApiResponse.success("Hospital process mapping deactivated successfully", null, HttpStatus.OK.value(), true));
    }
}
