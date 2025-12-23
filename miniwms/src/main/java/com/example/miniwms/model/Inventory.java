package com.example.miniwms.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    private String sku;

    private int availableQuantity;
    private int reservedQuantity;

    @Version
    private int version;

    public Inventory() {
    }

    public Inventory(String sku, int availableQuantity, int reservedQuantity) {
        this.sku = sku;
        this.availableQuantity = availableQuantity;
        this.reservedQuantity = reservedQuantity;
    }

    // Getters and Setters
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public int getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(int reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
