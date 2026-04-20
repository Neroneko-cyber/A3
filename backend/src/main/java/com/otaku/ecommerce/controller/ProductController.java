package com.otaku.ecommerce.controller;

import com.otaku.ecommerce.dto.ApiResponse;
import com.otaku.ecommerce.dto.ProductDTO;
import com.otaku.ecommerce.dto.ProductRequestDTO;
import com.otaku.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // ─── Get All Products (Public) ────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts() {
        return ResponseEntity.ok(ApiResponse.success("OTK-2010", "Produk berhasil diambil", productService.getAllProducts()));
    }

    // ─── Get Product by ID (Public) ───────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success("OTK-2011", "Detail produk berhasil diambil", productService.getProductById(id)));
    }

    // ─── Create Product (Admin) ───────────────────────────────────────────────
    @PostMapping
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@Valid @RequestBody ProductRequestDTO request) {
        ProductDTO created = productService.createProduct(request);
        return ResponseEntity.status(201)
                .body(ApiResponse.success("OTK-2012", "Produk berhasil ditambahkan", created));
    }

    // ─── Update Product (Admin) ───────────────────────────────────────────────
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(
            @PathVariable Integer id, @Valid @RequestBody ProductRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.success("OTK-2013", "Produk berhasil diperbarui", productService.updateProduct(id, request)));
    }

    // ─── Delete Product (Admin) ───────────────────────────────────────────────
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ApiResponse<Object>> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("OTK-2014", "Produk berhasil dihapus", null));
    }

    // ─── Update Stock Only (Admin) ────────────────────────────────────────────
    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ApiResponse<Object>> updateStock(
            @PathVariable Integer id, @RequestParam Integer quantity) {
        productService.updateStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success("OTK-2015", "Stok produk berhasil diperbarui", null));
    }
}
