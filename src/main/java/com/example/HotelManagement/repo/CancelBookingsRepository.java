package com.example.HotelManagement.repo;

import com.example.HotelManagement.entity.Bookings;
import com.example.HotelManagement.entity.CancelBookings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CancelBookingsRepository extends JpaRepository<CancelBookings, Long> {
    List<CancelBookings> findByUserEmail(String userEmail);
}
