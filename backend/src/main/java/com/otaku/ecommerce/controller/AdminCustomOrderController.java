package com.otaku.ecommerce.controller;

import com.otaku.ecommerce.dto.ApiResponse;
import com.otaku.ecommerce.dto.CustomOrderResponseDTO;
import com.otaku.ecommerce.service.CustomOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/custom-orders")
@PreAuthorize("hasRole('Admin')")
public class AdminCustomOrderController {

    @Autowired
    private CustomOrderService customOrderService;

    // ─── Get All Custom Orders ────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomOrderResponseDTO>>> getAllCustomOrders() {
        return ResponseEntity.ok(ApiResponse.success("OTK-2033", "Semua custom order berhasil diambil", customOrderService.getAllCustomOrders()));
    }

    // ─── Tetapkan Harga (Quoted) ──────────────────────────────────────────────
    @PatchMapping("/{id}/price")
    public ResponseEntity<ApiResponse<CustomOrderResponseDTO>> setPrice(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {

        BigDecimal price = new BigDecimal(body.get("price").toString());
        CustomOrderResponseDTO response = customOrderService.setPrice(id, price);
        return ResponseEntity.ok(ApiResponse.success("OTK-2034", "Harga custom order berhasil ditetapkan", response));
    }

    // ─── Update Status Custom Order ───────────────────────────────────────────
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<CustomOrderResponseDTO>> updateStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {

        String status = body.get("status");
        if (status == null || status.isBlank())
            return ResponseEntity.badRequest().body(ApiResponse.error("OTK-4010", "Status tidak boleh kosong"));

        CustomOrderResponseDTO response = customOrderService.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("OTK-2035", "Status custom order berhasil diperbarui", response));
    }
}
