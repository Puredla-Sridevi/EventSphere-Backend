package com.example.EventSphere.entity;

import com.example.EventSphere.enums.EventStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "event")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventId;
    private String eventTitle;
    private String description;
    private String location;
    @ManyToOne
    @JoinColumn(name="user_id")
    private Users createdBy;
    private LocalDateTime eventDate;
    private Integer totalSeats;
    private int availableSeats;
    private double price;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private EventStatus status;
    @PrePersist
    public void createdAt(){
        this.createdAt=LocalDateTime.now();
    }
}
