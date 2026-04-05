package com.example.EventSphere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="seatlock")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatLock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seatLockId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    private int seatsLocked;
    private LocalDateTime lockTime;
    private LocalDateTime expiryTime;
    @PrePersist
    public void lockTime(){
        this.lockTime=LocalDateTime.now();
    }
}
