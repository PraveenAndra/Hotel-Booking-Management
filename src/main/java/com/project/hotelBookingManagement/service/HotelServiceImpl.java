package com.project.hotelBookingManagement.service;
import com.project.hotelBookingManagement.dto.HotelDto;
import com.project.hotelBookingManagement.dto.HotelInfoDto;
import com.project.hotelBookingManagement.dto.RoomDto;
import com.project.hotelBookingManagement.entity.Hotel;
import com.project.hotelBookingManagement.entity.Room;
import com.project.hotelBookingManagement.exception.ResourceNotFoundException;
import com.project.hotelBookingManagement.repository.HotelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final RoomService roomService;


    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {

        log.info("Creating a new Hotel with name: {}",hotelDto.getName());
        Hotel hotel = modelMapper.map(hotelDto,Hotel.class);
        hotel.setActive(false);
        hotel = hotelRepository.save(hotel);

        log.info("Created a new hotel with ID:{}",hotel.getId());
        return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Getting the hotel with id:{}",id);
        Hotel hotel = hotelRepository.
                findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+ id));

        return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {
        log.info("Updating the hotel with the ID: {}", id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+id));
        modelMapper.map(hotelDto,hotel);
        hotel.setId(id);
        hotel = hotelRepository.save(hotel);
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+id));

        for(Room room: hotel.getRooms()){
            roomService.deleteRoomById(room.getId());
        }
        hotelRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void activateHotel(Long id) {
        log.info("Activating the hotel with id:{}",id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+ id));
        hotel.setActive(true);

//        Creating Inventory for all rooms in the hotel
        for (Room room: hotel.getRooms()){
            inventoryService.initializeRoomForYear(room);
        }
    }

    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId) {
        log.info("Getting the hotel details with id:{}",hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+ hotelId));
        List<RoomDto> rooms = hotel.getRooms()
                .stream()
                .map((room) -> modelMapper.map(room,RoomDto.class))
                .toList();
        return new HotelInfoDto(modelMapper.map(hotel,HotelDto.class),rooms);
    }


}
