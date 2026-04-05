package com.example.EventSphere.dtos;

import com.example.EventSphere.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BookingResponseDto {
    private int bookingId;
    private String eventTitle;
    private String userEmail;
    private int requiredSeats;
    private double price;
    private LocalDateTime bookingtime;
    private BookingStatus bookingStatus;
}
