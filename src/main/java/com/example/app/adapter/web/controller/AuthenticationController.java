package com.example.app.adapter.web.controller;

import com.example.app.adapter.web.request.LoginRequest;
import com.example.app.adapter.web.request.RefreshTokenRequest;
import com.example.app.adapter.web.request.RegisterRequest;
import com.example.app.adapter.web.response.AuthenticationResponse;
import com.example.app.adapter.web.response.UserResponse;
import com.example.app.application.mapper.UserMapper;
import com.example.app.common.response.CommonResponse;
import com.example.app.common.util.TraceIdGenerator;
import com.example.app.domain.model.User;
import com.example.app.domain.port.in.AuthenticationUseCase;
import com.example.app.domain.port.out.TokenRepositoryPort;
import com.example.app.infrastructure.security.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * REST Controller for authentication endpoints
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and registration endpoints")
public class AuthenticationController {
    
    private final AuthenticationUseCase authenticationUseCase;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepositoryPort tokenRepositoryPort;
    private final UserMapper userMapper;
    
    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Create a new user account")
    public ResponseEntity<CommonResponse<AuthenticationResponse>> register(@Valid @RequestBody RegisterRequest request) {
        String traceId = TraceIdGenerator.generate();
        log.info("[{}] Registration request for username: {}", traceId, request.getUsername());
        
        User user = authenticationUseCase.register(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName()
        );
        
        // Generate tokens
        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getUsername(),
                user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList())
        );
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());
        
        // Save refresh token
        tokenRepositoryPort.saveRefreshToken(user.getId(), refreshToken, jwtTokenProvider.getRefreshTokenExpiration());
        
        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getAccessTokenExpiration() / 1000)
                .user(userMapper.toResponse(user))
                .build();
        
        CommonResponse<AuthenticationResponse> response = CommonResponse.success(
                authResponse,
                "User registered successfully",
                HttpStatus.CREATED.value(),
                traceId
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate user and get JWT tokens")
    public ResponseEntity<CommonResponse<AuthenticationResponse>> login(@Valid @RequestBody LoginRequest request) {
        String traceId = TraceIdGenerator.generate();
        log.info("[{}] Login request for: {}", traceId, request.getUsernameOrEmail());
        
        User user = authenticationUseCase.authenticate(
                request.getUsernameOrEmail(),
                request.getPassword()
        );
        
        // Generate tokens
        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getUsername(),
                user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList())
        );
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());
        
        // Save refresh token
        tokenRepositoryPort.saveRefreshToken(user.getId(), refreshToken, jwtTokenProvider.getRefreshTokenExpiration());
        
        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getAccessTokenExpiration() / 1000)
                .user(userMapper.toResponse(user))
                .build();
        
        CommonResponse<AuthenticationResponse> response = CommonResponse.success(
                authResponse,
                "Login successful",
                HttpStatus.OK.value(),
                traceId
        );
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Get new access token using refresh token")
    public ResponseEntity<CommonResponse<AuthenticationResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        String traceId = TraceIdGenerator.generate();
        log.info("[{}] Token refresh request", traceId);
        
        User user = authenticationUseCase.validateRefreshToken(request.getRefreshToken());
        
        // Generate new access token
        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getUsername(),
                user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList())
        );
        
        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(request.getRefreshToken())
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getAccessTokenExpiration() / 1000)
                .user(userMapper.toResponse(user))
                .build();
        
        CommonResponse<AuthenticationResponse> response = CommonResponse.success(
                authResponse,
                "Token refreshed successfully",
                HttpStatus.OK.value(),
                traceId
        );
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Invalidate refresh token")
    public ResponseEntity<CommonResponse<Void>> logout(@Valid @RequestBody RefreshTokenRequest request) {
        String traceId = TraceIdGenerator.generate();
        log.info("[{}] Logout request", traceId);
        
        authenticationUseCase.invalidateRefreshToken(request.getRefreshToken());
        
        CommonResponse<Void> response = CommonResponse.success(
                null,
                "Logout successful",
                HttpStatus.OK.value(),
                traceId
        );
        
        return ResponseEntity.ok(response);
    }
}
