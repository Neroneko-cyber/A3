package com.otaku.ecommerce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Discounts")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DiscountID")
    private Integer id;

    @Column(name = "Code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "DiscountType", nullable = false, length = 20)
    private String discountType; // Percentage / Fixed

    @Column(name = "DiscountValue", nullable = false, precision = 18, scale = 2)
    private BigDecimal discountValue;

    @Column(name = "ApplicableCategory", length = 50)
    private String applicableCategory; // All, ActionFigure, CustomOutfit, Custom3D

    // --- Field baru untuk validasi bisnis (V6 migration) ---
    @Column(name = "MaxUsage")
    private Integer maxUsage; // null = unlimited

    @Column(name = "UsageCount")
    private Integer usageCount = 0;

    @Column(name = "ExpiryDate")
    private LocalDateTime expiryDate; // null = tidak ada batas waktu

    @Column(name = "IsActive")
    private Boolean isActive = true;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }
    public BigDecimal getDiscountValue() { return discountValue; }
    public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }
    public String getApplicableCategory() { return applicableCategory; }
    public void setApplicableCategory(String applicableCategory) { this.applicableCategory = applicableCategory; }
    public Integer getMaxUsage() { return maxUsage; }
    public void setMaxUsage(Integer maxUsage) { this.maxUsage = maxUsage; }
    public Integer getUsageCount() { return usageCount; }
    public void setUsageCount(Integer usageCount) { this.usageCount = usageCount; }
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
