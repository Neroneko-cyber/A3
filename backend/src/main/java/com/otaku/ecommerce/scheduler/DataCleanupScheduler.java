package com.otaku.ecommerce.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DataCleanupScheduler {

    private static final Logger logger = LoggerFactory.getLogger(DataCleanupScheduler.class);

    /**
     * Sapu Bersih: Auto-delete pesanan fiktif/pending lebih dari 3 hari.
     * Berjalan setiap jam tengah malam.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupOldPendingOrders() {
        logger.info("Scheduler Triggered: Membersihkan pesanan fiktif / pending lebih dari 3 hari...");
        // Stub implementation for executing deletes in CustomOrders/Orders table.
    }

    /**
     * Sapu Bersih: Auto-expired kode Diskon lama.
     * Berjalan setiap hari jam 1 pagi.
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void expireOldDiscountCodes() {
        logger.info("Scheduler Triggered: Menandai kode diskon lama sebagai auto-expired...");
        // Stub implementation for marking coupons as inactive.
    }
}
