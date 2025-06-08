package com.project.hotelBookingManagement.repository;

import com.project.hotelBookingManagement.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,Long> {
}
