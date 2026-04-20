package com.otaku.ecommerce.exception;

import com.otaku.ecommerce.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ─── Custom Business Exception ───────────────────────────────────────────
    @ExceptionHandler(CustomBusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusiness(CustomBusinessException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(ApiResponse.error(ex.getInternalCode(), ex.getMessage()));
    }

    // ─── Spring Security: Akses Ditolak ──────────────────────────────────────
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(403)
                .body(ApiResponse.error("OTK-4004", "Akses ditolak — tidak memiliki izin"));
    }

    // ─── Validasi @Valid Gagal ────────────────────────────────────────────────
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(400)
                .body(ApiResponse.error("OTK-4010", msg));
    }

    // ─── Upload / IO Error (Cloudinary gagal) ────────────────────────────────
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse<Object>> handleIO(IOException ex) {
        log.error("[UPLOAD-ERROR] Cloudinary IO failure: {}", ex.getMessage());
        return ResponseEntity.status(503)
                .body(ApiResponse.error("OTK-5001", "Layanan upload gambar sedang tidak tersedia"));
    }

    // ─── Fallback: Semua Exception Lain ──────────────────────────────────────
    // A09: Pesan internal TIDAK dikirim ke client — hanya dicatat di log server
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobal(Exception ex) {
        log.error("[SYSTEM-ERROR] Unhandled exception: ", ex);
        return ResponseEntity.status(500)
                .body(ApiResponse.error("OTK-5000", "Terjadi kesalahan pada server"));
    }
}
