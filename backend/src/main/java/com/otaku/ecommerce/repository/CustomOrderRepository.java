package com.otaku.ecommerce.repository;

import com.otaku.ecommerce.entity.CustomOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomOrderRepository extends JpaRepository<CustomOrder, Integer> {

    List<CustomOrder> findByUserEmail(String email);

    @Modifying
    @Query("UPDATE CustomOrder c SET c.status = :status WHERE c.id = :id")
    void updateStatus(Integer id, String status);
}
