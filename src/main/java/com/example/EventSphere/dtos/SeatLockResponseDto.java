package com.example.EventSphere.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class SeatLockResponseDto {
    private int seatLockId;
    private int eventId;
    private int seatsLocked;
    private LocalDateTime expiryTime;
    private String usermail;
}
