package com.otaku.ecommerce.security;

import com.otaku.ecommerce.repository.CustomOrderRepository;
import com.otaku.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * OrderSecurityEvaluator — Digunakan untuk @PreAuthorize ownership check.
 * Mencegah IDOR: User A tidak bisa akses resource milik User B.
 *
 * Penggunaan di Controller:
 *  @PreAuthorize("hasRole('Admin') or @orderSecurity.isOrderOwner(#id, authentication.name)")
 */
@Component("orderSecurity")
public class OrderSecurityEvaluator {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomOrderRepository customOrderRepository;

    /**
     * Cek apakah email yang diberikan adalah pemilik Order dengan ID tersebut.
     */
    public boolean isOrderOwner(Integer orderId, String email) {
        if (orderId == null || email == null) return false;
        return orderRepository.findById(orderId)
                .map(order -> order.getUser() != null && email.equals(order.getUser().getEmail()))
                .orElse(false);
    }

    /**
     * Cek apakah email yang diberikan adalah pemilik CustomOrder dengan ID tersebut.
     */
    public boolean isCustomOrderOwner(Integer customOrderId, String email) {
        if (customOrderId == null || email == null) return false;
        return customOrderRepository.findById(customOrderId)
                .map(co -> co.getUser() != null && email.equals(co.getUser().getEmail()))
                .orElse(false);
    }
}
