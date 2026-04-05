package com.example.EventSphere.service;

import com.example.EventSphere.EventSpecification;
import com.example.EventSphere.enums.EventStatus;
import com.example.EventSphere.dtos.EventRequestDto;
import com.example.EventSphere.dtos.EventResponseDto;
import com.example.EventSphere.entity.Event;
import com.example.EventSphere.entity.Users;
import com.example.EventSphere.repo.EventRepository;
import com.example.EventSphere.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
 private final EventSpecification eventSpecification;
    public Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public EventResponseDto createEvent(EventRequestDto eventRequestDto) throws BadRequestException {
         if(eventRequestDto.getTitle()==null){
             throw new IllegalArgumentException("Title is Empty");
         }
        if(eventRequestDto.getPrice() < 0){
            throw new IllegalArgumentException("Price cannot be negative");
        }
         if(eventRequestDto.getTotalSeats()<=0){
             throw new IllegalArgumentException("No Sats Available");
         }
         if(eventRequestDto.getEventDateTime().isBefore(LocalDateTime.now())){
             throw new IllegalArgumentException("Event Expired");
         }
         Users user=userRepository.findByEmail(getAuthentication().getName()).orElseThrow(()->new UsernameNotFoundException("No User found"));
         Event event=Event.builder()
                 .eventTitle(eventRequestDto.getTitle())
                 .description(eventRequestDto.getDescription())
                 .location(eventRequestDto.getLocation())
                 .eventDate(eventRequestDto.getEventDateTime())
                 .totalSeats(eventRequestDto.getTotalSeats())
                 .availableSeats(eventRequestDto.getTotalSeats())
                 .status(EventStatus.UPCOMING)
                 .createdBy(user)
                 .price(eventRequestDto.getPrice())
                 .build();
          event.setCreatedBy(user);
         eventRepository.save(event);
         return EventResponseDto.builder()
                 .id(event.getEventId())
                 .title(event.getEventTitle())
                 .description(event.getDescription())
                 .location(event.getLocation())
                 .eventDateTime(event.getEventDate())
                 .totalSeats(event.getTotalSeats())
                 .availableSeats(event.getAvailableSeats())
                 .price(event.getPrice())
                 .eventStatus(event.getStatus())
                 .createdAt(event.getCreatedAt())
                 .createdBy(user.getEmail())
                 .build();
    }


    public Page<EventResponseDto> getAllEvents(int pageNo, int pageSize,String location, Double minPrice, Double maxPrice, EventStatus eventStatus, String sortBy, String direction) {
        Specification<Event> specification=eventSpecification.filterEvents(location,minPrice,maxPrice,eventStatus);
        Sort sort= Sort.by(sortBy != null ? sortBy :"eventDate");
        if("desc".equalsIgnoreCase(direction)){
            sort=sort.descending();
        }else{
            sort=sort.ascending();
        }
        Page<Event> events= eventRepository.findAll(specification,PageRequest.of(pageNo,pageSize,sort));
        return  events.map( event->EventResponseDto.builder()
                    .id(event.getEventId())
                    .title(event.getEventTitle())
                    .description(event.getDescription())
                    .location(event.getLocation())
                    .eventDateTime(event.getEventDate())
                    .totalSeats(event.getTotalSeats())
                    .availableSeats(event.getAvailableSeats())
                    .price(event.getPrice())
                    .eventStatus(event.getStatus())
                    .createdAt(event.getCreatedAt())
                    .createdBy(event.getCreatedBy().getEmail())
                    .build());
    }

    public EventResponseDto getEventById(int id) {
        Event event=eventRepository.findById(id).orElseThrow(()->new RuntimeException("Event Not Found with id : "+id));
     return   EventResponseDto.builder()
                .id(event.getEventId())
                .title(event.getEventTitle())
                .description(event.getDescription())
                .location(event.getLocation())
                .eventDateTime(event.getEventDate())
                .totalSeats(event.getTotalSeats())
                .availableSeats(event.getAvailableSeats())
                .price(event.getPrice())
                .eventStatus(event.getStatus())
                .createdAt(event.getCreatedAt())
                .createdBy(event.getCreatedBy().getEmail())
                .build();
    }

    public EventResponseDto updateEvent(int id, EventRequestDto requestDto) {
          Event event=eventRepository.findById(id).orElseThrow(()->new NoSuchElementException("No Such Elemnt Found WIth Id: "+id));
          if(! (event.getCreatedBy().getEmail().equals(getAuthentication().getName())
                  || getAuthentication().getAuthorities().stream().anyMatch(auth->auth.getAuthority().equals("ROLE_ADMIN")))){
              throw new RuntimeException("You are not allowed to update this event");          }
          if(requestDto.getTitle()==null){
              throw new IllegalArgumentException("Title is Empty");
          }
          if(requestDto.getTotalSeats()<=0){
              throw new IllegalArgumentException("No Seats Avaliable");
          }
          if(requestDto.getEventDateTime().isBefore(LocalDateTime.now())){
              throw new IllegalArgumentException("Event time should future ");
          }
          if(event.getEventDate().isBefore(LocalDateTime.now())){
              event.setStatus(EventStatus.COMPLETED);
          }else{
              event.setStatus(EventStatus.UPCOMING);
          }
          event.setEventDate(requestDto.getEventDateTime());
          event.setEventTitle(requestDto.getTitle());
          event.setDescription(requestDto.getDescription());
          event.setLocation(requestDto.getLocation());
          event.setPrice(requestDto.getPrice());
          event.setTotalSeats(requestDto.getTotalSeats());
          event.setAvailableSeats(requestDto.getTotalSeats());
          Event event1 =eventRepository.save(event);
            return   EventResponseDto.builder()
                .id(event1.getEventId())
                .title(event1.getEventTitle())
                .description(event1.getDescription())
                .location(event1.getLocation())
                .eventDateTime(event1.getEventDate())
                .totalSeats(event1.getTotalSeats())
                .availableSeats(event1.getAvailableSeats())
                .price(event1.getPrice())
                .eventStatus(event1.getStatus())
                .createdAt(event1.getCreatedAt())
                .createdBy(event1.getCreatedBy().getEmail())
                .build();
    }

    public EventResponseDto deleteEvent(int id) {
        Event event=eventRepository.findById(id).orElseThrow(()->new NoSuchElementException("No Such Event With Id: "+id));
        if(! (event.getCreatedBy().getEmail().equals(getAuthentication().getName())
                || getAuthentication().getAuthorities().stream().anyMatch(auth->auth.getAuthority().equals("ROLE_ADMIN")))){
            throw  new RuntimeException("You Are Not Allowed To Delete This Event");
        }
        if(event.getStatus().equals(EventStatus.CANCELLED)){
            throw new RuntimeException("Event already cancelled");
        }
        event.setStatus(EventStatus.CANCELLED);
      Event deleteEvent=eventRepository.save(event);
        return   EventResponseDto.builder()
                .id(deleteEvent.getEventId())
                .title(deleteEvent.getEventTitle())
                .description(deleteEvent.getDescription())
                .location(deleteEvent.getLocation())
                .eventDateTime(deleteEvent.getEventDate())
                .totalSeats(deleteEvent.getTotalSeats())
                .availableSeats(deleteEvent.getAvailableSeats())
                .price(deleteEvent.getPrice())
                .eventStatus(deleteEvent.getStatus())
                .createdAt(deleteEvent.getCreatedAt())
                .createdBy(deleteEvent.getCreatedBy().getEmail())
                .build();
    }


}

