package com.otaku.ecommerce.controller;

import com.otaku.ecommerce.dto.ApiResponse;
import com.otaku.ecommerce.service.CloudinaryUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/upload")
public class ImageUploadController {

    @Autowired
    private CloudinaryUploadService uploadService;

    /** Endpoint #1 — Referensi gambar Custom Outfit Order */
    @PostMapping("/outfit-reference")
    @PreAuthorize("hasAnyRole('Customer', 'Admin')")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadOutfitReference(
            @RequestParam("file") MultipartFile file) throws IOException {

        String imageUrl = uploadService.uploadOutfitReference(file);
        return ResponseEntity.ok(ApiResponse.success(
                "OTK-2031",
                "Gambar referensi outfit berhasil diupload",
                Map.of("imageUrl", imageUrl)
        ));
    }

    /** Endpoint #2 — Referensi gambar Custom 3D Action Figure Order */
    @PostMapping("/figure-reference")
    @PreAuthorize("hasAnyRole('Customer', 'Admin')")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadFigureReference(
            @RequestParam("file") MultipartFile file) throws IOException {

        String imageUrl = uploadService.uploadFigureReference(file);
        return ResponseEntity.ok(ApiResponse.success(
                "OTK-2032",
                "Gambar referensi action figure berhasil diupload",
                Map.of("imageUrl", imageUrl)
        ));
    }
}
