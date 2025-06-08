package com.project.hotelBookingManagement.service;

import com.project.hotelBookingManagement.dto.BookingDto;
import com.project.hotelBookingManagement.dto.BookingRequest;
import com.project.hotelBookingManagement.dto.GuestDto;

import java.util.List;

public interface BookingService {

    BookingDto initializeBooking(BookingRequest bookingRequest);
    BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);
}
