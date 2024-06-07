package com.example.HotelManagement.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
public class CancelBookings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    private String guestName;
    private String userEmail;
    private String roomNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
}
