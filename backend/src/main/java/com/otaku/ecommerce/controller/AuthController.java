package com.otaku.ecommerce.controller;

import com.otaku.ecommerce.dto.ApiResponse;
import com.otaku.ecommerce.dto.LoginRequestDTO;
import com.otaku.ecommerce.dto.RegisterRequestDTO;
import com.otaku.ecommerce.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(
            @Valid @RequestBody LoginRequestDTO request,
            HttpServletRequest httpRequest) {

        Map<String, Object> result = authService.login(request, httpRequest);
        String accessToken = (String) result.get("accessToken");

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .body(ApiResponse.success("OTK-2001", "Login berhasil", result));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> register(@Valid @RequestBody RegisterRequestDTO request) {
        authService.register(request);
        return ResponseEntity.status(201)
                .body(ApiResponse.success("OTK-2002", "Registrasi berhasil, silakan login.", null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Map<String, Object>>> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        if (refreshToken == null || refreshToken.isBlank())
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("OTK-4010", "refreshToken tidak boleh kosong"));
        Map<String, Object> result = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(ApiResponse.success("OTK-2003", "Token berhasil diperbarui", result));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            Authentication authentication) {

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            return ResponseEntity.badRequest().body(ApiResponse.error("OTK-4010", "Token tidak valid"));

        String token = authHeader.substring(7);

        // Ambil userId dari database via email di JWT (authentication.name = email)
        String email = authentication.getName();
        com.otaku.ecommerce.dto.UserDTO user = authService.findByEmail(email);
        authService.logout(token, user.getId());

        return ResponseEntity.ok(ApiResponse.success("OTK-2004", "Logout berhasil", null));
    }
}
