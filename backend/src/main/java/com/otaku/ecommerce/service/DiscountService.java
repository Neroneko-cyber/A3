package com.otaku.ecommerce.service;

import com.otaku.ecommerce.dto.DiscountRequestDTO;
import com.otaku.ecommerce.dto.DiscountResponseDTO;
import com.otaku.ecommerce.entity.Discount;
import com.otaku.ecommerce.exception.CustomBusinessException;
import com.otaku.ecommerce.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    // ─── Validate Discount (Customer — response hanya expose field aman) ──────
    public DiscountResponseDTO validateDiscount(String code) {
        Discount d = discountRepository.findByCode(code)
                .orElseThrow(() -> new CustomBusinessException("OTK-4043", "Kode diskon tidak valid", 400));

        if (!Boolean.TRUE.equals(d.getIsActive()))
            throw new CustomBusinessException("OTK-4092", "Kode diskon sudah tidak aktif", 400);
        if (d.getExpiryDate() != null && LocalDateTime.now().isAfter(d.getExpiryDate()))
            throw new CustomBusinessException("OTK-4093", "Kode diskon sudah kadaluarsa", 400);
        if (d.getMaxUsage() != null && d.getUsageCount() >= d.getMaxUsage())
            throw new CustomBusinessException("OTK-4094", "Kuota kode diskon sudah habis", 409);

        return toDTO(d);
    }

    // ─── Create Discount (Admin) ──────────────────────────────────────────────
    public DiscountResponseDTO createDiscount(DiscountRequestDTO request) {
        if (discountRepository.findByCode(request.getCode()).isPresent())
            throw new CustomBusinessException("OTK-4006", "Kode diskon sudah ada", 409);

        Discount d = new Discount();
        d.setCode(request.getCode());
        d.setDiscountType(request.getDiscountType());
        d.setDiscountValue(request.getDiscountValue());
        d.setApplicableCategory(request.getApplicableCategory());
        d.setMaxUsage(request.getMaxUsage());
        d.setUsageCount(0);
        d.setExpiryDate(request.getExpiryDate());
        d.setIsActive(Boolean.TRUE.equals(request.getIsActive()));
        return toDTO(discountRepository.save(d));
    }

    // ─── Update Discount (Admin) ──────────────────────────────────────────────
    public DiscountResponseDTO updateDiscount(Integer id, DiscountRequestDTO request) {
        if (id == null) throw new CustomBusinessException("OTK-4010", "ID tidak boleh kosong", 400);
        Discount d = discountRepository.findById(id)
                .orElseThrow(() -> new CustomBusinessException("OTK-4043", "Kode diskon tidak ditemukan", 404));
        d.setDiscountType(request.getDiscountType());
        d.setDiscountValue(request.getDiscountValue());
        d.setApplicableCategory(request.getApplicableCategory());
        d.setMaxUsage(request.getMaxUsage());
        d.setExpiryDate(request.getExpiryDate());
        d.setIsActive(request.getIsActive());
        return toDTO(discountRepository.save(d));
    }

    // ─── Delete (Nonaktifkan) Discount (Admin) ────────────────────────────────
    @Transactional
    public void deleteDiscount(Integer id) {
        if (id == null) throw new CustomBusinessException("OTK-4010", "ID tidak boleh kosong", 400);
        Discount d = discountRepository.findById(id)
                .orElseThrow(() -> new CustomBusinessException("OTK-4043", "Kode diskon tidak ditemukan", 404));
        d.setIsActive(false);
        discountRepository.save(d);
    }

    // ─── Helper ───────────────────────────────────────────────────────────────
    private DiscountResponseDTO toDTO(Discount d) {
        DiscountResponseDTO dto = new DiscountResponseDTO();
        dto.setCode(d.getCode());
        dto.setDiscountType(d.getDiscountType());
        dto.setDiscountValue(d.getDiscountValue());
        dto.setApplicableCategory(d.getApplicableCategory());
        dto.setIsActive(d.getIsActive());
        return dto;
    }
}
