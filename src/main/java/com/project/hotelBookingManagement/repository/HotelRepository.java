package com.project.hotelBookingManagement.repository;


import com.project.hotelBookingManagement.entity.Hotel;
import com.project.hotelBookingManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,Long> {
    List<Hotel> findByOwner(User owner);
}
