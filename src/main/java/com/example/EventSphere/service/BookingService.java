package com.example.EventSphere.service;

import com.example.EventSphere.entity.*;
import com.example.EventSphere.enums.EventStatus;
import com.example.EventSphere.enums.BookingStatus;
import com.example.EventSphere.dtos.BookingResponseDto;
import com.example.EventSphere.dtos.MailDto;
import com.example.EventSphere.enums.PaymentStatus;
import com.example.EventSphere.repo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final IdempotencyRepo idempotencyRepo;
    private final  BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final SeatLockRepository seatLockRepository;
    private final EmailService emailService;
    private  final PaymentService paymentService;
    private final ObjectMapper objectMapper;
    private Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
    @Transactional
    public BookingResponseDto confirmBooking(int seatLockId,String idempotencykey) throws Exception {
//        if(bookingRequestDto.getRequiredSeats()<=0){
//            throw new BadRequestException("Required seats should be greater than 0");
//        }
        Optional<Idempotency> exists=idempotencyRepo.findByIdempotencyKey(idempotencykey);
        if(!(exists.isEmpty())){
          String storedResponse= exists.get().getResponse();
          BookingResponseDto bookingResponseDto=objectMapper.readValue(storedResponse,BookingResponseDto.class);
          return bookingResponseDto;
        }
        SeatLock seatLock=seatLockRepository.findById(seatLockId).orElseThrow(()->new NoSuchElementException("No Reservation done with Id: "+seatLockId));
        if(! seatLock.getUser().getEmail().equals(getAuthentication().getName())){
            throw new AccessDeniedException("You Are Not Allowed To Confirm It");
        }
        if(seatLock.getExpiryTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Reservation Expired");
        }
        Users user=seatLock.getUser();
        Event event=seatLock.getEvent();
//        Thread.sleep(10000); // 5 seconds
        if(!event.getStatus().equals(EventStatus.UPCOMING)){
            throw new BadRequestException("Event is cancelled or not available");
        }
        double price=event.getPrice()* seatLock.getSeatsLocked();
        PaymentStatus paymentStatus=paymentService.processPayment(price);
        if(paymentStatus==PaymentStatus.FAILED){
            throw new BadRequestException("Payment Failed, Please try again");
        }
        Booking booking=Booking.builder()
                .event(event)
                .users(user)
                .bookingStatus(BookingStatus.CONFIRMED)
                .numberOfSeats(seatLock.getSeatsLocked())
                .build();
        booking.setTotalPrice(price);
        Booking savedBooking=bookingRepository.save(booking);

        emailService.sendBookingConfirmation(
                MailDto.builder()
                        .toEmail(user.getEmail())
                        .eventName(event.getEventTitle())
                        .price(booking.getTotalPrice())
                        .seats(seatLock.getSeatsLocked())
                        .build()
        );
        seatLockRepository.deleteById(seatLockId);
        BookingResponseDto responseDto= BookingResponseDto.builder()
                .bookingId(savedBooking.getBookingId())
                .bookingtime(savedBooking.getBookingTime())
                .eventTitle(savedBooking.getEvent().getEventTitle())
                .requiredSeats(savedBooking.getNumberOfSeats())
                .bookingStatus(savedBooking.getBookingStatus())
                .userEmail(savedBooking.getUsers().getEmail())
                .price(savedBooking.getTotalPrice())
                .build();
        try{
            Idempotency idempotency=Idempotency.builder()
                    .idempotencyKey(idempotencykey)
                    .status(200)
                    .response(objectMapper.writeValueAsString(responseDto))
                    .build();
            idempotencyRepo.save(idempotency);
        }catch(Exception e){
            Optional<Idempotency> existing=idempotencyRepo.findByIdempotencyKey(idempotencykey);
            if(existing.isPresent()){
                return objectMapper.readValue(existing.get().getResponse(),BookingResponseDto.class);
            }
        }

        return responseDto;
    }
    public Page<BookingResponseDto> getMyEvents(int pageNo, int pageSize){
        Users users=userRepository.findByEmail(getAuthentication().getName()).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
        Page<Booking> bookings=bookingRepository.findAllByUsers(users, PageRequest.of(pageNo,pageSize));
//        if(bookings.isEmpty()){
//            throw new NoSuchElementException("No Bookings found for user: "+users.getEmail());
//        }
       return bookings.map(
               booking ->  BookingResponseDto.builder()
                       .bookingId(booking.getBookingId())
                       .bookingtime(booking.getBookingTime())
                       .eventTitle(booking.getEvent().getEventTitle())
                       .requiredSeats(booking.getNumberOfSeats())
                       .bookingStatus(booking.getBookingStatus())
                       .userEmail(booking.getUsers().getEmail())
                       .price(booking.getTotalPrice())
                       .build()
       );
    }
    @Transactional
    public BookingResponseDto cancelBooking(int bookingId) throws Exception {
        Booking booking=bookingRepository.findById(bookingId).orElseThrow(()->new NoSuchElementException("No Booking Found With Id: "+bookingId));
        if(! booking.getBookingStatus().equals(BookingStatus.CONFIRMED)){
            throw new BadRequestException("Only confirmed bookings can be cancelled ");
        }
        if(!(booking.getUsers().getEmail().equals(getAuthentication().getName()) ||
        getAuthentication().getAuthorities().stream().anyMatch(auth->auth.getAuthority().equals("ROLE_ADMIN"))
        )){
            throw new AccessDeniedException("Not Allowed To cancel the events");
        }
        Event event=booking.getEvent();
        event.setAvailableSeats(booking.getNumberOfSeats()+event.getAvailableSeats());
        eventRepository.save(event);
        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        return BookingResponseDto.builder()
                .bookingId(booking.getBookingId())
                .bookingtime(booking.getBookingTime())
                .eventTitle(booking.getEvent().getEventTitle())
                .requiredSeats(booking.getNumberOfSeats())
                .bookingStatus(booking.getBookingStatus())
                .userEmail(booking.getUsers().getEmail())
                .price(booking.getTotalPrice())
                .build();
    }
}
