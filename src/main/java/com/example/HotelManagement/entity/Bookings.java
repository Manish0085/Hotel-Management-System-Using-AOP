package com.example.HotelManagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
public class Bookings implements Serializable {

    public static final int key=1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String guestName;
    private String userEmail;
    private String roomNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;




    @Override
    public String toString() {
        return "Bookings{" +
                "id=" + id +
                ", userEmail='" + userEmail + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", price=" + price +
                ", roomType=" + roomType +
                '}';
    }
}