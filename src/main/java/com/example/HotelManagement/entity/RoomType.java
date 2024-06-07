package com.example.HotelManagement.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "room_types")
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "price_per_hour")
    private Double pricePerHour;

    // Getters and setters



}