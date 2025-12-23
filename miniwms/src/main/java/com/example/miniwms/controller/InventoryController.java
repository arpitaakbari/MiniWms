package com.example.miniwms.controller;

import com.example.miniwms.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    static class ReceiveRequest {
        public String sku;
        public int quantity;
    }

    static class ReserveRequest {
        public String sku;
        public int quantity;
        public String orderId;
    }

    @PostMapping("/receiveOrder")
    public ResponseEntity<?> receive(@RequestBody ReceiveRequest req) {
        try {
            inventoryService.receiveItem(req.sku, req.quantity);
            return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "message", "Order Receive successfully",
                    "Sku", req.sku,
                    "Quantity", req.quantity
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "ERROR",
                    "message", e.getMessage()));
        }
    }

    @PostMapping("/reserveOrder")
    public ResponseEntity<?> reserve(@RequestBody ReserveRequest req) {
        try {
            inventoryService.reserveItem(req.sku, req.quantity, req.orderId);
            return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "message", "Order Reserve successfully",
                    "Sku", req.sku,
                    "OrderId", req.orderId,
                    "Quantity", req.quantity
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "ERROR",
                    "message", e.getMessage()));
        }
    }
}
