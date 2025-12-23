package com.example.miniwms.repository;

import com.example.miniwms.model.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdempotencyRepository extends JpaRepository<IdempotencyKey, String> {
}
