package com.example.miniwms.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "idempotency")
public class IdempotencyKey {

    @Id
    private String requestId;  // UUID as primary key

    private String orderId;

    @Lob
    private String response;

    public IdempotencyKey() {
        this.requestId = UUID.randomUUID().toString(); // auto-generate UUID
    }

    public IdempotencyKey(String orderId, String response) {
        this.requestId = UUID.randomUUID().toString(); // auto-generate UUID
        this.orderId = orderId;
        this.response = response;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
