package com.example.HotelManagement.service;

import com.example.HotelManagement.dto.BookingDTO;
import com.example.HotelManagement.entity.Bookings;
import com.example.HotelManagement.entity.CancelBookings;
import com.example.HotelManagement.entity.RoomType;
import com.example.HotelManagement.repo.BookingRepository;
import com.example.HotelManagement.repo.CancelBookingsRepository;
import com.example.HotelManagement.repo.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.Date;
import java.sql.Time;
import java.time.*;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private CancelBookingsRepository cancelBookingsRepository;

    public static int Deluxe=2;
    public static int AC=3;
    public static int Normal=5;

    public Bookings createBooking(BookingDTO bookingDTO) {
        // Convert BookingDTO to Booking entity
        Bookings booking = new Bookings();
        booking.setGuestName(bookingDTO.getGuestName());
        booking.setUserEmail(bookingDTO.getUserEmail());
        booking.setRoomNumber(bookingDTO.getRoomNumber());
        booking.setStartTime(bookingDTO.getStartTime());
        booking.setEndTime(bookingDTO.getEndTime());
        RoomType roomType = roomTypeRepository.findByName(bookingDTO.getRoomType());
        String type=roomType.getName();
        booking.setRoomType(roomType);

        // Calculate price based on room type's price per hour
        Double durationHours = calculateDurationHours(booking.getStartTime(), booking.getEndTime());
        Double totalPrice = durationHours * booking.getRoomType().getPricePerHour();
        booking.setPrice(totalPrice);

        Long roomTypeId = roomType.getId();
        List<Bookings> allBookingbyType = bookingRepository.findAllBookingbyType(roomTypeId);
        // Save booking to database
        if(type.equalsIgnoreCase("Deluxe") && allBookingbyType.size()<2)
        {
            return bookingRepository.save(booking);
        }
        else if(type.equalsIgnoreCase("AC") && allBookingbyType.size()<3)
        {
            return bookingRepository.save(booking);
        }
        else if(type.equalsIgnoreCase("Normal") && allBookingbyType.size()<5)
        {
            return bookingRepository.save(booking);
        }


        throw new RuntimeException("Rooms Are full");
    }

    private Double calculateDurationHours(LocalDateTime startTime, LocalDateTime endTime) {
        return (double) Duration.between(startTime, endTime).toHours();
    }

    public List<Bookings> findOverlappingBookings(String roomNumber, LocalDateTime startTime, LocalDateTime endTime) {
        return bookingRepository.findOverlappingBookings(roomNumber, startTime, endTime);
    }

    public List<Bookings> getAllBooking() {

        return bookingRepository.findAll();
    }

    public String cancelBooking(String roomNumber, String guestName, String userEmail) {
        Bookings entity = (Bookings) bookingRepository.findByRoomNumberAndGuestNameAndUserEmail(roomNumber, guestName, userEmail).orElseThrow(() -> new EntityNotFoundException("Entity not found"));

        LocalDateTime localDateTime1 = entity.getStartTime();
        LocalDateTime localDateTime2 = LocalDateTime.now();

        // Convert LocalDateTime to ZonedDateTime at the system default time zone
        ZonedDateTime zonedDateTime1 = localDateTime1.atZone(ZoneId.systemDefault());
        ZonedDateTime zonedDateTime2 = localDateTime2.atZone(ZoneId.systemDefault());

        // Convert ZonedDateTime to epoch seconds
        long epochSeconds1 = zonedDateTime1.toEpochSecond();
        long epochSeconds2 = zonedDateTime2.toEpochSecond();
        if(epochSeconds1 <= epochSeconds2 ){
            return "You can,t cancel the booking ";
        }

        else {
            // Transfer the data to Library2
            CancelBookings book = new CancelBookings();
            book.setGuestName(entity.getGuestName());
            book.setUserEmail(entity.getUserEmail());
            book.setRoomNumber(entity.getRoomNumber());
            book.setStartTime(entity.getStartTime());
            book.setEndTime(entity.getEndTime());
            book.setStatus("Cancelled");
            cancelBookingsRepository.save(book);

            bookingRepository.delete(entity);

            return "Booking has been cancelled successfully";
        }
        //return "Hii";
    }

    public Bookings getBookingByUserEmail(String userEmail) {
       return bookingRepository.getBookingByUserEmail(userEmail);

    }

//    public CancelBookings getCancelBookingRecordByUserEmail(String userEmail) {
//        return cancelBookingsRepository.findByUserEmail(userEmail);
//    }

    public List<CancelBookings> findByUserEmail(String userEmail) {
        // Assuming you have a method in your repository to retrieve multiple cancel bookings by user email
        List<CancelBookings> bookings = cancelBookingsRepository.findByUserEmail(userEmail);

        // If no bookings found, you can either return an empty list or throw an EntityNotFoundException
        if (bookings.isEmpty()) {
            throw new EntityNotFoundException("No cancel bookings found for user email: " + userEmail);
        }

        return bookings;
    }
}