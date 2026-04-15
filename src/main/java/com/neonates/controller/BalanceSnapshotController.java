package com.neonates.controller;

import com.neonates.request.BalanceSnapshotRequestDTO;
import com.neonates.response.ApiResponse;
import com.neonates.response.BalanceSnapshotResponseDTO;
import com.neonates.service.BalanceSnapshotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/balance-snapshots")
@RequiredArgsConstructor
@CrossOrigin
public class BalanceSnapshotController {

    private final BalanceSnapshotService balanceSnapshotService;

    // POST /api/balance-snapshots
    @PostMapping
    public ResponseEntity<ApiResponse<BalanceSnapshotResponseDTO>> createSnapshot(@Valid @RequestBody BalanceSnapshotRequestDTO request) {
        BalanceSnapshotResponseDTO data = balanceSnapshotService.createSnapshot(request);
        return new ResponseEntity<>(ApiResponse.success("Balance snapshot created successfully", data, HttpStatus.CREATED.value(), true), HttpStatus.CREATED);
    }

    // GET /api/balance-snapshots/date/{dateId}
    @GetMapping("/date/{dateId}")
    public ResponseEntity<ApiResponse<List<BalanceSnapshotResponseDTO>>> getSnapshotsByDate(@PathVariable Integer dateId) {
        List<BalanceSnapshotResponseDTO> data = balanceSnapshotService.getSnapshotsByDate(dateId);
        return ResponseEntity.ok(ApiResponse.success("Balance snapshots fetched successfully", data, HttpStatus.OK.value(), true));
    }

    // GET /api/balance-snapshots/latest
    @GetMapping("/latest")
    public ResponseEntity<ApiResponse<BalanceSnapshotResponseDTO>> getLatestSnapshot() {
        BalanceSnapshotResponseDTO data = balanceSnapshotService.getLatestSnapshot();
        return ResponseEntity.ok(ApiResponse.success("Latest balance snapshot fetched successfully", data, HttpStatus.OK.value(), true));
    }

    // GET /api/balance-snapshots/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<BalanceSnapshotResponseDTO>>> getSnapshotsByUser(@PathVariable Long userId) {
        List<BalanceSnapshotResponseDTO> data = balanceSnapshotService.getSnapshotsByUser(userId);
        return ResponseEntity.ok(ApiResponse.success("Balance snapshots fetched successfully", data, HttpStatus.OK.value(), true));
    }
}
