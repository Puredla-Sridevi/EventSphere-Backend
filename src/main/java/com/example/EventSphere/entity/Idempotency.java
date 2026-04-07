package com.example.EventSphere.entity;

import com.example.EventSphere.dtos.BookingResponseDto;
import com.example.EventSphere.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "idempotency_keys")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Idempotency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idempotencyId;
    @Column(unique = true)
    private String idempotencyKey;
    @Lob
    private String  response;
    private int status;
    private LocalDateTime createdAt;
    @PrePersist
    public void createdAt(){
        this.createdAt=LocalDateTime.now();
    }
}
