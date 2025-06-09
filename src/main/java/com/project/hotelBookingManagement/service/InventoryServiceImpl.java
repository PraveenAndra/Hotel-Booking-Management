package com.project.hotelBookingManagement.service;


import com.project.hotelBookingManagement.dto.HotelDto;
import com.project.hotelBookingManagement.dto.HotelPriceDto;
import com.project.hotelBookingManagement.dto.HotelSearchRequest;
import com.project.hotelBookingManagement.entity.Hotel;
import com.project.hotelBookingManagement.entity.Inventory;
import com.project.hotelBookingManagement.entity.Room;
import com.project.hotelBookingManagement.repository.HotelMinPriceRepository;
import com.project.hotelBookingManagement.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService{

    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final HotelMinPriceRepository hotelMinPriceRepository;

    @Override
    public void initializeRoomForYear(Room room) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime endDate = today.plusYears(1);
        for(; !today.isAfter(endDate); today = today.plusDays(1)){
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .bookCount(0)
                    .reservedCount(0)
                    .city(room.getHotel().getCity())
                    .date(today)
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.ONE)
                    .totalCount(room.getTotalCount())
                    .closed(false)
                    .build();
            inventoryRepository.save(inventory);
        }
    }

    @Override
    public void deleteAllInventories(Room room) {
        log.info("Deleting all Inventories for room id: {}",room.getId());
        inventoryRepository.deleteByRoom(room);
    }

    @Override
    public Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest) {

        log.info("Searching hotels for {} city, from {} to {}", hotelSearchRequest.getCity(),hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate());
        Pageable pageable = PageRequest.of(hotelSearchRequest.getPage(),hotelSearchRequest.getSize());
        Long dateCount = ChronoUnit.DAYS.between(hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate()) + 1;

        //Business logic for 90 Days
        Page<HotelPriceDto> hotelPage = hotelMinPriceRepository.findHotelsWithAvailableInventory(
                hotelSearchRequest.getCity(),
                hotelSearchRequest.getStartDate(),
                hotelSearchRequest.getEndDate(),
                hotelSearchRequest.getRoomsCount(),
                dateCount,pageable
        );
//        Page<Hotel> hotelPage = inventoryRepository.findHotelsWithAvailableInventory(
//                hotelSearchRequest.getCity(),
//                hotelSearchRequest.getStartDate(),
//                hotelSearchRequest.getEndDate(),
//                hotelSearchRequest.getRoomsCount(),
//                dateCount,pageable
//        );
        return hotelPage;

    }
}
