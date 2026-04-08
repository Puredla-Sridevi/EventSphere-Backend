package com.example.EventSphere.dtos;

import com.example.EventSphere.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class EventResponseDto {
    private Integer id;
    private String title;
    private String description;
    private LocalDateTime eventDateTime;
    private String location;
    private int totalSeats;
    private int availableSeats;
    private double price;
    private EventStatus eventStatus;
    private String createdBy;
    private LocalDateTime createdAt;
}
