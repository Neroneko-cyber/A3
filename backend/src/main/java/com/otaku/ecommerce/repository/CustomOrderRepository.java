package com.otaku.ecommerce.repository;

import com.otaku.ecommerce.entity.CustomOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomOrderRepository extends JpaRepository<CustomOrder, Integer> {
    List<CustomOrder> findByUserId(Integer userId);
}
