package com.example.HotelManagement.controller;

import com.example.HotelManagement.dto.BookingDTO;
import com.example.HotelManagement.entity.Bookings;
import com.example.HotelManagement.entity.CancelBookings;
import com.example.HotelManagement.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/create")
    public Bookings createBooking(@RequestBody BookingDTO bookingDTO) {
        return bookingService.createBooking(bookingDTO);
    }

    @GetMapping("/checkOverlap")
    public boolean checkOverlap(
            @RequestParam String roomNumber,
            @RequestParam String startTime, // Parse as String
            @RequestParam String endTime     // Parse as String
    ) {
        LocalDateTime startDateTime = LocalDateTime.parse(startTime);
        LocalDateTime endDateTime = LocalDateTime.parse(endTime);

        List<Bookings> overlappingBookings = bookingService.findOverlappingBookings(roomNumber, startDateTime, endDateTime);
        return !overlappingBookings.isEmpty();
    }

    @GetMapping("/getBooking")
    public ResponseEntity<Bookings> getBookingByUserEmail(@RequestParam(value = "userEmail") String userEmail){
        try {
            Bookings booking = bookingService.getBookingByUserEmail(userEmail);
            return ResponseEntity.ok(booking);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getCancelBookingRecord")
    public ResponseEntity<List<CancelBookings>> getCancelBookingRecordByUserEmail(@RequestParam(value = "userEmail") String userEmail) {
        try {
            List<CancelBookings> bookings = bookingService.findByUserEmail(userEmail);
            if (bookings.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
            } else {
                return ResponseEntity.ok(bookings);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @DeleteMapping("/cancelBooking")
    public ResponseEntity<String> cancelBooking(
            @RequestParam(value = "roomNumber") String roomNumber,
            @RequestParam(value = "guestName") String guestName,
            @RequestParam(value = "userEmail") String userEmail) {

        try {
            String result = bookingService.cancelBooking(roomNumber, guestName, userEmail);
            return ResponseEntity.ok(result);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @GetMapping("/allBookings")
    public List<Bookings> getAllBooking()
    {
        return bookingService.getAllBooking();
    }
}