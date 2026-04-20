package com.otaku.ecommerce.scheduler;

import com.otaku.ecommerce.entity.Order;
import com.otaku.ecommerce.repository.DiscountRepository;
import com.otaku.ecommerce.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataCleanupScheduler {

    private static final Logger logger = LoggerFactory.getLogger(DataCleanupScheduler.class);

    @Autowired private OrderRepository    orderRepository;
    @Autowired private DiscountRepository discountRepository;

    /**
     * Setiap tengah malam — bersihkan order Pending yang lebih dari 3 hari.
     * Order yang tidak dibayar/diproses dalam 3 hari dianggap batal.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cleanupOldPendingOrders() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(3);
        List<Order> staleOrders = orderRepository.findByStatusAndCreatedAtBefore("Pending", cutoff);
        if (!staleOrders.isEmpty()) {
            orderRepository.deleteAll(staleOrders);
            logger.info("[SCHEDULER] Dihapus {} order Pending yang sudah > 3 hari", staleOrders.size());
        } else {
            logger.info("[SCHEDULER] Tidak ada order Pending lama yang perlu dibersihkan.");
        }
    }

    /**
     * Setiap jam 01:00 — nonaktifkan kode diskon yang sudah melewati ExpiryDate.
     */
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void expireOldDiscountCodes() {
        discountRepository.deactivateExpiredDiscounts(LocalDateTime.now());
        logger.info("[SCHEDULER] Kode diskon yang kadaluarsa berhasil dinonaktifkan.");
    }
}
