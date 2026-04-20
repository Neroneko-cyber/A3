package com.otaku.ecommerce.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.otaku.ecommerce.exception.CustomBusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * CloudinaryUploadService — Terpusat untuk kedua endpoint upload.
 * Multi-layer defense: validasi Spring → upload Cloudinary → return secure_url.
 */
@Service
public class CloudinaryUploadService {

    private static final Logger log = LoggerFactory.getLogger(CloudinaryUploadService.class);

    // ─── Format & Ukuran yang diizinkan ───────────────────────────────────────
    private static final List<String> ALLOWED_MIME  = List.of("image/png", "image/jpeg");
    private static final long         MAX_BYTES      = 5L * 1024 * 1024; // 5 MB

    @Autowired
    private Cloudinary cloudinary;

    @Value("${cloudinary.outfit-folder}") private String outfitFolder;
    @Value("${cloudinary.figure-folder}") private String figureFolder;

    // ─── Upload Outfit Reference ──────────────────────────────────────────────
    public String uploadOutfitReference(MultipartFile file) throws IOException {
        validateFile(file);
        String url = doUpload(file, outfitFolder, "outfit_", 400, 400, "fill");
        log.info("[UPLOAD-OK] Outfit reference uploaded: {}", url);
        return url;
    }

    // ─── Upload Figure Reference ──────────────────────────────────────────────
    public String uploadFigureReference(MultipartFile file) throws IOException {
        validateFile(file);
        String url = doUpload(file, figureFolder, "figure_", 800, 800, "fit");
        log.info("[UPLOAD-OK] Figure reference uploaded: {}", url);
        return url;
    }

    // ─── Private: Eksekusi Upload ke Cloudinary ───────────────────────────────
    private String doUpload(MultipartFile file, String folder, String prefix,
                            int width, int height, String cropMode) throws IOException {
        Map<?, ?> result = cloudinary.uploader().upload(
            file.getBytes(),
            ObjectUtils.asMap(
                "folder",          folder,
                "resource_type",   "image",
                "allowed_formats", new String[]{"png", "jpg", "jpeg"},
                "public_id",       prefix + UUID.randomUUID(),
                "overwrite",       false,
                "invalidate",      true,
                "strip_metadata",  true,  // Hapus EXIF/GPS metadata
                "eager", ObjectUtils.asMap(
                    "width", width, "height", height, "crop", cropMode
                )
            )
        );
        return (String) result.get("secure_url");
    }

    // ─── Private: Validasi File ────────────────────────────────────────────────
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty())
            throw new CustomBusinessException("OTK-4101", "File tidak boleh kosong.", 400);

        if (file.getContentType() == null || !ALLOWED_MIME.contains(file.getContentType()))
            throw new CustomBusinessException("OTK-4099",
                "Format tidak didukung. Hanya PNG, JPG, dan JPEG yang diizinkan.", 400);

        if (file.getSize() > MAX_BYTES)
            throw new CustomBusinessException("OTK-4100",
                "Ukuran file melebihi batas maksimal 5 MB.", 400);
    }
}
