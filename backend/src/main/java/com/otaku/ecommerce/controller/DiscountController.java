package com.otaku.ecommerce.controller;

import com.otaku.ecommerce.dto.ApiResponse;
import com.otaku.ecommerce.dto.DiscountRequestDTO;
import com.otaku.ecommerce.dto.DiscountResponseDTO;
import com.otaku.ecommerce.service.DiscountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    // ─── Validate Discount (Customer/Public) — return DTO, bukan entity ──────
    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<DiscountResponseDTO>> validateDiscount(@RequestParam String code) {
        DiscountResponseDTO response = discountService.validateDiscount(code);
        return ResponseEntity.ok(ApiResponse.success("OTK-2040", "Kode diskon valid", response));
    }

    // ─── Create Discount (Admin) ──────────────────────────────────────────────
    @PostMapping
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ApiResponse<DiscountResponseDTO>> createDiscount(@Valid @RequestBody DiscountRequestDTO request) {
        DiscountResponseDTO created = discountService.createDiscount(request);
        return ResponseEntity.status(201)
                .body(ApiResponse.success("OTK-2041", "Kode diskon berhasil dibuat", created));
    }

    // ─── Update Discount (Admin) ──────────────────────────────────────────────
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ApiResponse<DiscountResponseDTO>> updateDiscount(
            @PathVariable Integer id, @Valid @RequestBody DiscountRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.success("OTK-2042", "Kode diskon berhasil diperbarui", discountService.updateDiscount(id, request)));
    }

    // ─── Delete (Soft) Discount (Admin) ──────────────────────────────────────
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ApiResponse<Object>> deleteDiscount(@PathVariable Integer id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.ok(ApiResponse.success("OTK-2043", "Kode diskon berhasil dinonaktifkan", null));
    }
}
