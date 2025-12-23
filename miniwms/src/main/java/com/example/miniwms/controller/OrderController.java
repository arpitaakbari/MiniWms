package com.example.miniwms.controller;

import com.example.miniwms.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    static class ReceiveRequest {
        public String orderId;
    }

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/shipOrder")
    public ResponseEntity<?> ship(@RequestBody ReceiveRequest req) {
        try {
            orderService.shipOrder(req.orderId);
            return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "message", "Order shipped successfully",
                    "orderId", req.orderId
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "ERROR",
                    "message", e.getMessage(),
                    "orderId", req.orderId
            ));
        }
    }
}
