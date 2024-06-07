package com.example.HotelManagement.repo;

import com.example.HotelManagement.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    RoomType findByName(String name);
}