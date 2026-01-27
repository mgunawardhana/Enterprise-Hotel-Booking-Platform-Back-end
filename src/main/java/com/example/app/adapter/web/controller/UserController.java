package com.example.app.adapter.web.controller;

import com.example.app.adapter.web.request.UpdateUserRequest;
import com.example.app.adapter.web.response.UserResponse;
import com.example.app.application.mapper.UserMapper;
import com.example.app.common.constants.Constants;
import com.example.app.common.response.CommonResponse;
import com.example.app.common.response.PageResponse;
import com.example.app.common.util.TraceIdGenerator;
import com.example.app.domain.model.User;
import com.example.app.domain.port.in.UserManagementUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST Controller for user management endpoints
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "User CRUD operations (Admin only)")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    
    private final UserManagementUseCase userManagementUseCase;
    private final UserMapper userMapper;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users", description = "Retrieve paginated list of users (Admin only)")
    public ResponseEntity<CommonResponse<PageResponse<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        String traceId = TraceIdGenerator.generate();
        log.info("[{}] Get all users - page: {}, size: {}", traceId, page, size);
        
        // Validate page size
        if (size > Constants.MAX_PAGE_SIZE) {
            size = Constants.MAX_PAGE_SIZE;
        }
        
        List<User> users = userManagementUseCase.getAllUsers(page, size);
        long totalElements = userManagementUseCase.countUsers();
        
        List<UserResponse> userResponses = users.stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
        
        PageResponse<UserResponse> pageResponse = PageResponse.of(userResponses, page, size, totalElements);
        
        CommonResponse<PageResponse<UserResponse>> response = CommonResponse.success(
                pageResponse,
                "Users retrieved successfully",
                HttpStatus.OK.value(),
                traceId
        );
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId.toString() == authentication.principal.username")
    @Operation(summary = "Get user by ID", description = "Retrieve user details by ID")
    public ResponseEntity<CommonResponse<UserResponse>> getUserById(@PathVariable UUID userId) {
        String traceId = TraceIdGenerator.generate();
        log.info("[{}] Get user by ID: {}", traceId, userId);
        
        User user = userManagementUseCase.getUserById(userId);
        UserResponse userResponse = userMapper.toResponse(user);
        
        CommonResponse<UserResponse> response = CommonResponse.success(
                userResponse,
                "User retrieved successfully",
                HttpStatus.OK.value(),
                traceId
        );
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId.toString() == authentication.principal.username")
    @Operation(summary = "Update user", description = "Update user information")
    public ResponseEntity<CommonResponse<UserResponse>> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserRequest request) {
        
        String traceId = TraceIdGenerator.generate();
        log.info("[{}] Update user: {}", traceId, userId);
        
        User user = userManagementUseCase.updateUser(
                userId,
                request.getFirstName(),
                request.getLastName(),
                request.getEmail()
        );
        
        UserResponse userResponse = userMapper.toResponse(user);
        
        CommonResponse<UserResponse> response = CommonResponse.success(
                userResponse,
                "User updated successfully",
                HttpStatus.OK.value(),
                traceId
        );
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user", description = "Soft delete user (Admin only)")
    public ResponseEntity<CommonResponse<Void>> deleteUser(@PathVariable UUID userId) {
        String traceId = TraceIdGenerator.generate();
        log.info("[{}] Delete user: {}", traceId, userId);
        
        userManagementUseCase.deleteUser(userId);
        
        CommonResponse<Void> response = CommonResponse.success(
                null,
                "User deleted successfully",
                HttpStatus.OK.value(),
                traceId
        );
        
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{userId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Toggle user status", description = "Enable or disable user account (Admin only)")
    public ResponseEntity<CommonResponse<UserResponse>> toggleUserStatus(
            @PathVariable UUID userId,
            @RequestParam boolean enabled) {
        
        String traceId = TraceIdGenerator.generate();
        log.info("[{}] Toggle user status: {} to {}", traceId, userId, enabled);
        
        User user = userManagementUseCase.toggleUserStatus(userId, enabled);
        UserResponse userResponse = userMapper.toResponse(user);
        
        CommonResponse<UserResponse> response = CommonResponse.success(
                userResponse,
                "User status updated successfully",
                HttpStatus.OK.value(),
                traceId
        );
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{userId}/roles/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Assign role", description = "Assign role to user (Admin only)")
    public ResponseEntity<CommonResponse<UserResponse>> assignRole(
            @PathVariable UUID userId,
            @PathVariable String roleName) {
        
        String traceId = TraceIdGenerator.generate();
        log.info("[{}] Assign role {} to user {}", traceId, roleName, userId);
        
        User user = userManagementUseCase.assignRole(userId, roleName);
        UserResponse userResponse = userMapper.toResponse(user);
        
        CommonResponse<UserResponse> response = CommonResponse.success(
                userResponse,
                "Role assigned successfully",
                HttpStatus.OK.value(),
                traceId
        );
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{userId}/roles/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remove role", description = "Remove role from user (Admin only)")
    public ResponseEntity<CommonResponse<UserResponse>> removeRole(
            @PathVariable UUID userId,
            @PathVariable String roleName) {
        
        String traceId = TraceIdGenerator.generate();
        log.info("[{}] Remove role {} from user {}", traceId, roleName, userId);
        
        User user = userManagementUseCase.removeRole(userId, roleName);
        UserResponse userResponse = userMapper.toResponse(user);
        
        CommonResponse<UserResponse> response = CommonResponse.success(
                userResponse,
                "Role removed successfully",
                HttpStatus.OK.value(),
                traceId
        );
        
        return ResponseEntity.ok(response);
    }
}
