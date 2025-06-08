package com.project.hotelBookingManagement.controller;


import com.project.hotelBookingManagement.dto.BookingDto;
import com.project.hotelBookingManagement.dto.BookingRequest;
import com.project.hotelBookingManagement.dto.GuestDto;
import com.project.hotelBookingManagement.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/bookings")
public class HotelBookingController {


    private final BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<BookingDto> initializeBooking(@RequestBody BookingRequest bookingRequest){

        return ResponseEntity.ok(bookingService.initializeBooking(bookingRequest));
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<BookingDto> addGuests(@PathVariable Long bookingId, @RequestBody List<GuestDto> guestDtoList){
        return ResponseEntity.ok(bookingService.addGuests(bookingId,guestDtoList));
    }
}
