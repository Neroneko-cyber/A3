package com.otaku.ecommerce.dto;

import java.math.BigDecimal;

/**
 * DiscountResponseDTO — Menggantikan return entitas JPA Discount langsung ke response publik.
 * Hanya expose field yang aman: code, type, value, category.
 * Tidak mengekspos DiscountID atau field internal lainnya.
 */
public class DiscountResponseDTO {

    private String code;
    private String discountType;
    private BigDecimal discountValue;
    private String applicableCategory;
    private Boolean isActive;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }
    public BigDecimal getDiscountValue() { return discountValue; }
    public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }
    public String getApplicableCategory() { return applicableCategory; }
    public void setApplicableCategory(String applicableCategory) { this.applicableCategory = applicableCategory; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
