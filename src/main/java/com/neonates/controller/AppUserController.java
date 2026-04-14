package com.neonates.controller;

import com.neonates.Enum.UserRole;
import com.neonates.request.AppUserRequestDTO;
import com.neonates.response.ApiResponse;
import com.neonates.response.AppUserResponseDTO;
import com.neonates.service.AppUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin
public class AppUserController {

    private final AppUserService appUserService;

    // POST /api/users
    @PostMapping
    public ResponseEntity<ApiResponse<AppUserResponseDTO>> createUser(@Valid @RequestBody AppUserRequestDTO request) {
        AppUserResponseDTO data = appUserService.createUser(request);
        return new ResponseEntity<>(ApiResponse.success("User created successfully", data, HttpStatus.CREATED.value(), true), HttpStatus.CREATED);
    }

    // PUT /api/users/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AppUserResponseDTO>> updateUser(@PathVariable Long id, @Valid @RequestBody AppUserRequestDTO request) {
        AppUserResponseDTO data = appUserService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", data, HttpStatus.OK.value(), true));
    }

    // GET /api/users
    @GetMapping
    public ResponseEntity<ApiResponse<List<AppUserResponseDTO>>> getAllUsers() {
        List<AppUserResponseDTO> data = appUserService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users fetched successfully", data, HttpStatus.OK.value(), true));
    }

    // GET /api/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppUserResponseDTO>> getUserById(@PathVariable Long id) {
        AppUserResponseDTO data = appUserService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User fetched successfully", data, HttpStatus.OK.value(), true));
    }

    // GET /api/users/role/{role}
    @GetMapping("/role/{role}")
    public ResponseEntity<ApiResponse<List<AppUserResponseDTO>>> getUsersByRole(@PathVariable UserRole role) {
        List<AppUserResponseDTO> data = appUserService.getUsersByRole(role);
        return ResponseEntity.ok(ApiResponse.success("Users fetched successfully", data, HttpStatus.OK.value(), true));
    }

    // DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@PathVariable Long id) {
        appUserService.deactivateUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deactivated successfully", null, HttpStatus.OK.value(), true));
    }
}
