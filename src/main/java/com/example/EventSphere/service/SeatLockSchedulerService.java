package com.example.EventSphere.service;

import com.example.EventSphere.entity.Event;
import com.example.EventSphere.entity.SeatLock;
import com.example.EventSphere.repo.EventRepository;
import com.example.EventSphere.repo.SeatLockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatLockSchedulerService {
    private final SeatLockRepository seatLockRepository;
    private final EventRepository eventRepository;
    @Scheduled(fixedRate =30000)
    @Transactional
    public void releaseExpiredSeatLocks(){
        LocalDateTime currentTime=LocalDateTime.now();
        List<SeatLock> seatLocks=seatLockRepository.findAllExpiredSeats(currentTime);
        if(seatLocks.isEmpty()){
            return;
        }
        for(SeatLock seatLock:seatLocks){
            Event event=seatLock.getEvent();
            int seatsLocked=seatLock.getSeatsLocked();
            event.setAvailableSeats(event.getAvailableSeats()+seatsLocked);
            eventRepository.save(event);

        }
        seatLockRepository.deleteAll(seatLocks);
        System.out.println("number locks expired : "+seatLocks.size());
    }
}
