package com.example.EventSphere.repo;

import com.example.EventSphere.entity.Idempotency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdempotencyRepo extends JpaRepository<Idempotency,Integer> {

    Optional<Idempotency> findByIdempotencyKey(String key);
}
