package com.otaku.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/upload")
public class ImageUploadController {

    /**
     * Endpoint untuk meng-intercept file gambar Custom 3D Action Figure (Spring Web Multipart).
     * Sesuai instruksi backend_plan.md, nantinya URL gambar akan ditautkan ke tabel CustomOrders.
     */
    @PostMapping("/custom-figure")
    public ResponseEntity<String> uploadCustomFigureImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty.");
        }

        // Stub logic: intercept and return mock URL
        String mockGeneratedUrl = "https://ecommerce.otaku.local/images/custom/" + file.getOriginalFilename();

        return ResponseEntity.ok("File received and will be linked to CustomOrders. URL: " + mockGeneratedUrl);
    }
}
