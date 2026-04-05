package com.example.EventSphere.repo;

import com.example.EventSphere.dtos.BookingResponseDto;
import com.example.EventSphere.entity.Booking;
import com.example.EventSphere.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Integer> {
   public Page<Booking> findAllByUsers(Users users, PageRequest pageable);
}
