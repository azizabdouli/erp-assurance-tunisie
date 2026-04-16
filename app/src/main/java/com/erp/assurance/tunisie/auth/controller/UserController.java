package com.erp.assurance.tunisie.auth.controller;

import com.erp.assurance.tunisie.auth.dto.UserResponse;
import com.erp.assurance.tunisie.auth.service.UserService;
import com.erp.assurance.tunisie.shared.dto.ApiResponse;
import com.erp.assurance.tunisie.shared.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Admin operations for user accounts")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "List all users (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<UserResponse> response = userService.getAllUsers(PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable UUID id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a user account (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> deactivateUser(@PathVariable UUID id) {
        UserResponse response = userService.deactivateUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deactivated", response));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a user account (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> activateUser(@PathVariable UUID id) {
        UserResponse response = userService.activateUser(id);
        return ResponseEntity.ok(ApiResponse.success("User activated", response));
    }
}
