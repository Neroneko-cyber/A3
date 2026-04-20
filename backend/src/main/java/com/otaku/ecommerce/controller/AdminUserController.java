package com.otaku.ecommerce.controller;

import com.otaku.ecommerce.dto.ApiResponse;
import com.otaku.ecommerce.dto.UserDTO;
import com.otaku.ecommerce.service.AuthService;
import com.otaku.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/users")
@PreAuthorize("hasRole('Admin')")
public class AdminUserController {

    @Autowired private UserService  userService;
    @Autowired private AuthService  authService;

    // ─── Get All Users ────────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success("OTK-2050", "Daftar user berhasil diambil", userService.getAllUsers()));
    }

    // ─── Get User by ID ───────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success("OTK-2051", "Detail user berhasil diambil", userService.getUserById(id)));
    }

    // ─── Change Role ──────────────────────────────────────────────────────────
    @PatchMapping("/{id}/role")
    public ResponseEntity<ApiResponse<UserDTO>> changeRole(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {

        String newRole = body.get("role");
        if (newRole == null || newRole.isBlank())
            return ResponseEntity.badRequest().body(ApiResponse.error("OTK-4010", "Role tidak boleh kosong"));

        return ResponseEntity.ok(ApiResponse.success("OTK-2052", "Role user berhasil diubah", userService.changeRole(id, newRole)));
    }

    // ─── Force Logout User ────────────────────────────────────────────────────
    @PostMapping("/{id}/force-logout")
    public ResponseEntity<ApiResponse<Object>> forceLogout(@PathVariable Integer id) {
        authService.forceLogoutUser(id);
        return ResponseEntity.ok(ApiResponse.success("OTK-2053", "User " + id + " berhasil di-force-logout", null));
    }

    // ─── Deactivate User ──────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deactivateUser(@PathVariable Integer id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok(ApiResponse.success("OTK-2054", "Akun user berhasil dinonaktifkan", null));
    }
}
