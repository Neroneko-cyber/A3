package com.otaku.ecommerce.repository;

import com.otaku.ecommerce.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {

    Optional<Discount> findByCode(String code);

    @Modifying
    @Query("UPDATE Discount d SET d.isActive = false WHERE d.expiryDate IS NOT NULL AND d.expiryDate < :now")
    void deactivateExpiredDiscounts(LocalDateTime now);
}
