package com.example.EventSphere.controller;

import com.example.EventSphere.dtos.BookingResponseDto;
import com.example.EventSphere.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("bookings")
public class BookingController {
    private final BookingService bookingService;
    @PostMapping("/confirm")
    public ResponseEntity<BookingResponseDto>  confirmBooking(@RequestParam int seatLockId) throws Exception {
        return new ResponseEntity<>(bookingService.confirmBooking(seatLockId), HttpStatus.CREATED);
    }

@GetMapping("/my")
    public ResponseEntity<Page<BookingResponseDto>> getMyEvents(@RequestParam(required = false,defaultValue = "0") int pageNo, @RequestParam(defaultValue = "2",required = false) int pageSize){
        return ResponseEntity.ok(bookingService.getMyEvents(pageNo,pageSize));
}
@PutMapping("/cancel/{id}")
    public ResponseEntity<BookingResponseDto> cancelBooking(@PathVariable("id") int bookingId) throws Exception {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId));
}
}
