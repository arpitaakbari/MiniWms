package com.example.miniwms.service;

import com.example.miniwms.model.IdempotencyKey;
import com.example.miniwms.model.Reservation;
import com.example.miniwms.model.Inventory;
import com.example.miniwms.repository.IdempotencyRepository;
import com.example.miniwms.repository.InventoryRepository;
import com.example.miniwms.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final ReservationRepository reservationRepo;
    private final InventoryRepository inventoryRepo;
    private final IdempotencyRepository idempotencyRepository;
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    public OrderService(ReservationRepository reservationRepo, InventoryRepository inventoryRepo, IdempotencyRepository idempotencyRepository) {
        this.reservationRepo = reservationRepo;
        this.inventoryRepo = inventoryRepo;
        this.idempotencyRepository = idempotencyRepository;
    }

    @Transactional
    public void shipOrder(String orderId) {
        log.info("TAG1 Starting shipOrder for orderId={}", orderId);

        List<Reservation> reservations = reservationRepo.findByOrderId(orderId);

        if (reservations.isEmpty()) {
            log.warn("TAG2 No reservations found for orderId={}", orderId);
            throw new RuntimeException("Order not found");
        }

        for (Reservation res : reservations) {
            log.info("TAG3 Processing reservation id={}, sku={}, qty={}",
                    res.getId(), res.getSku(), res.getQuantity());

            Inventory inv = inventoryRepo.lockBySku(res.getSku());

            if (inv == null) {
                log.error("TAG4 Inventory not found for sku={}", res.getSku());
                throw new RuntimeException("Inventory not found");
            }

            int beforeQty = inv.getReservedQuantity();
            int afterQty = beforeQty - res.getQuantity();

            log.info("TAG5 Updating inventory sku={}, reservedQty {} -> {}",
                    res.getSku(), beforeQty, afterQty);

            inv.setReservedQuantity(afterQty);
            inventoryRepo.save(inv);

            res.setStatus("SHIPPED");
            reservationRepo.save(res);

            log.info("TAG6 Reservation id={} marked as SHIPPED", res.getId());

//            String requestId = UUID.randomUUID().toString();
            IdempotencyKey idempotencyKey =
                    new IdempotencyKey();
            idempotencyKey.setOrderId(orderId);
            idempotencyKey.setResponse("SHIPPED");

            idempotencyRepository.save(idempotencyKey);

            log.info("TAG7 Idempotency key saved for orderId={}", orderId);
        }

        log.info("TAG8 shipOrder completed successfully for orderId={}", orderId);
    }
}
