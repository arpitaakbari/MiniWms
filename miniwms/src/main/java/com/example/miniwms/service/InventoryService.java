package com.example.miniwms.service;

import com.example.miniwms.model.Inventory;
import com.example.miniwms.model.Reservation;
import com.example.miniwms.repository.InventoryRepository;
import com.example.miniwms.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepo;
    private final ReservationRepository reservationRepo;

    public InventoryService(InventoryRepository inventoryRepo, ReservationRepository reservationRepo) {
        this.inventoryRepo = inventoryRepo;
        this.reservationRepo = reservationRepo;
    }

    @Transactional
    public void receiveItem(String sku, int quantity) {
        Inventory inv = inventoryRepo.findById(sku).orElse(new Inventory(sku, 0, 0));
        inv.setAvailableQuantity(inv.getAvailableQuantity() + quantity);
        inventoryRepo.save(inv);
    }

    @Transactional
    public void reserveItem(String sku, int quantity, String orderId) {
        Inventory inv = inventoryRepo.lockBySku(sku);
        if (inv == null) throw new RuntimeException("SKU not found");
        if (inv.getAvailableQuantity() < quantity) throw new RuntimeException("Insufficient inventory");

        inv.setAvailableQuantity(inv.getAvailableQuantity() - quantity);
        inv.setReservedQuantity(inv.getReservedQuantity() + quantity);
        inventoryRepo.save(inv);

        Reservation res = new Reservation(orderId, sku, quantity, "RESERVED");
        reservationRepo.save(res);
    }
}
