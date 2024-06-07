package com.example.HotelManagement.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class BookingDTO {
    private Long id;
    private String guestName;
    private String userEmail;
    private String roomNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double price;

    private String roomType;

    // Getters and setters


}