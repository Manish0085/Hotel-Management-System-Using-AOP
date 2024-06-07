package com.example.HotelManagement.entity;

import com.example.HotelManagement.entity.RoomType;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number")
    private String roomNumber;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private RoomType roomType;

    // Getters and setters



}