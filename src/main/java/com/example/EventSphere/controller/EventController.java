package com.example.EventSphere.controller;

import com.example.EventSphere.enums.EventStatus;
import com.example.EventSphere.dtos.EventRequestDto;
import com.example.EventSphere.dtos.EventResponseDto;
import com.example.EventSphere.service.EventService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(@RequestBody EventRequestDto requestDto) throws BadRequestException {
        return new ResponseEntity(eventService.createEvent(requestDto), HttpStatus.CREATED);
    }
     @GetMapping("/allEvents")
    public ResponseEntity<Page<EventResponseDto>> getAllEvents(@RequestParam(defaultValue = "0",required = false) int pageNo, @RequestParam(defaultValue = "5",required = false) int pageSize
             , @RequestParam(required = false) String location
     , @RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice
             , @RequestParam(required = false) EventStatus eventStatus,
                @RequestParam(required = false) String sortBy,@RequestParam(required = false) String direction                                               ){
        return ResponseEntity.ok(eventService.getAllEvents(pageNo,pageSize,location,minPrice,maxPrice,eventStatus,sortBy,direction));
     }

     @GetMapping("{id}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable("id") int id)
     {
         return ResponseEntity.ok(eventService.getEventById(id));
     }

     @PutMapping("{id}")
    public ResponseEntity<EventResponseDto> updateEvent(@PathVariable("id") int id,@RequestBody EventRequestDto requestDto){
       return ResponseEntity.ok(eventService.updateEvent(id,requestDto));
     }
     @DeleteMapping("{id}")
     public ResponseEntity<EventResponseDto> deleteEvent(@PathVariable("id") int id) throws InterruptedException {
        return ResponseEntity.ok(eventService.deleteEvent(id));
     }
}
