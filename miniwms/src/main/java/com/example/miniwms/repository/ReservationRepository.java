package com.example.miniwms.repository;

import com.example.miniwms.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByOrderId(String orderId);
}
