package com.example.EventSphere.repo;

import com.example.EventSphere.entity.SeatLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SeatLockRepository extends JpaRepository<SeatLock,Integer> {
    @Query("SELECT s FROM SeatLock s WHERE s.expiryTime< :currentTime")
    public List<SeatLock> findAllExpiredSeats(@Param("currentTime")LocalDateTime currentTime);
}
