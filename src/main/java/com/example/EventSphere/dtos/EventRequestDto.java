package com.example.EventSphere.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class EventRequestDto {
    private String title;
    private String description;
    private String location;
    private LocalDateTime eventDateTime;
    private int totalSeats;
    private double price;
}
