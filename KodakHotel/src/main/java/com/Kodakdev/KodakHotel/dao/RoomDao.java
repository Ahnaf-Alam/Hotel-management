package com.Kodakdev.KodakHotel.dao;

import com.Kodakdev.KodakHotel.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RoomDao extends JpaRepository<Room, Long> {

    @Query("select distinct r.roomType from Room r")
    List<String> findDistinctRoomTypes();

    @Query("select r from Room r where r.roomType like %:roomType% and r.id not in (select b.room.id from Booking b where" +
            "(b.checkInDate <= :checkOutDate) and (b.checkOutDate >= :checkInDate))")
    List<Room> findAvailableRoomsByDatesAndTypes(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

    @Query("select r from Room r where r.id in (select b.room.id from Booking b)")
    List<Room> getAvailableRooms();
}
