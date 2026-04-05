package com.example.EventSphere.service;

import com.example.EventSphere.enums.EventStatus;
import com.example.EventSphere.dtos.BookingRequestDto;
import com.example.EventSphere.dtos.SeatLockResponseDto;
import com.example.EventSphere.entity.Event;
import com.example.EventSphere.entity.SeatLock;
import com.example.EventSphere.entity.Users;
import com.example.EventSphere.repo.EventRepository;
import com.example.EventSphere.repo.SeatLockRepository;
import com.example.EventSphere.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SeatLockService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final SeatLockRepository seatLockRepository;
    public Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
    @Transactional
    public SeatLockResponseDto lockSeats(BookingRequestDto bookingRequestDto) throws BadRequestException {
        Users user=userRepository.findByEmail(getAuthentication().getName()).orElseThrow(()->new UsernameNotFoundException("No User Found"));
        Event event=eventRepository.findByIdForUpdate(bookingRequestDto.getEventId()).orElseThrow(()->new NoSuchElementException("NO Event Found WIth Id: "+bookingRequestDto.getEventId()));
        if(event.getAvailableSeats()<bookingRequestDto.getRequiredSeats()){
            throw new BadRequestException("No Seats Available");
        }
        if(! event.getStatus().equals(EventStatus.UPCOMING)){
            throw new BadRequestException("Event Already done..");
        }
        event.setAvailableSeats(event.getAvailableSeats()-bookingRequestDto.getRequiredSeats());
        eventRepository.save(event);
        SeatLock seatLock=SeatLock.builder()
                .seatsLocked(bookingRequestDto.getRequiredSeats())
                .event(event)
                .user(user)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .build();
        SeatLock savedSeatLock=seatLockRepository.save(seatLock);
        return SeatLockResponseDto.builder()
                .seatLockId(savedSeatLock.getSeatLockId())
                .seatsLocked(savedSeatLock.getSeatsLocked())
                .eventId(savedSeatLock.getEvent().getEventId())
                .expiryTime(savedSeatLock.getExpiryTime())
                .usermail(savedSeatLock.getUser().getEmail())
                .build();
    }
}
