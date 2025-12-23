package com.example.miniwms.model;

import jakarta.persistence.*;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue
    private Long id;

    private String orderId;
    private String sku;
    private int quantity;
    private String status; // RESERVED, SHIPPED

    public Reservation() {
    }

    public Reservation(String orderId, String sku, int quantity, String status) {
        this.orderId = orderId;
        this.sku = sku;
        this.quantity = quantity;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getSku() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}