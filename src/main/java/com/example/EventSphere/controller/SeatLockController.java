package com.example.EventSphere.controller;

import com.example.EventSphere.dtos.BookingRequestDto;
import com.example.EventSphere.dtos.SeatLockResponseDto;
import com.example.EventSphere.service.SeatLockService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("seatlock")
@RequiredArgsConstructor
public class SeatLockController {
    private final SeatLockService seatLockService;
    @PostMapping("/lock")
    public ResponseEntity<SeatLockResponseDto> lockSeats(@RequestBody BookingRequestDto bookingRequestDto) throws BadRequestException {
    return  ResponseEntity.ok(seatLockService.lockSeats(bookingRequestDto));
    }
}
