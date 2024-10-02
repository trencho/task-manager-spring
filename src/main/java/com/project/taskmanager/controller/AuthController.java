package com.project.taskmanager.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.taskmanager.dto.RefreshTokenRequestDTO;
import com.project.taskmanager.dto.TokenResponseDTO;
import com.project.taskmanager.dto.UserLoginDTO;
import com.project.taskmanager.dto.UserRegistrationDTO;
import com.project.taskmanager.mapper.UserMapper;
import com.project.taskmanager.security.JwtTokenProvider;
import com.project.taskmanager.service.RefreshTokenService;
import com.project.taskmanager.service.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody final UserRegistrationDTO userRegistrationDTO) {
        try {
            userService.registerUser(userMapper.toEntity(userRegistrationDTO));
            return ResponseEntity.ok("User registered successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody final UserLoginDTO userLoginDTO) {
        try {
            final var username = userLoginDTO.username();
            final var authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, userLoginDTO.password()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            final var accessToken = tokenProvider.generateAccessToken(username);
            final var refreshTokenEntity = refreshTokenService.createRefreshToken(username);

            return ResponseEntity.ok(new TokenResponseDTO(accessToken, refreshTokenEntity.getToken()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestBody final RefreshTokenRequestDTO refreshTokenRequestDTO) {
        try {
            return ResponseEntity.ok(refreshTokenService.refreshAccessToken(refreshTokenRequestDTO.refreshToken()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
