package com.Kodakdev.KodakHotel.service;

import com.Kodakdev.KodakHotel.dao.BookingDao;
import com.Kodakdev.KodakHotel.dao.RoomDao;
import com.Kodakdev.KodakHotel.dto.Response;
import com.Kodakdev.KodakHotel.dto.RoomDTO;
import com.Kodakdev.KodakHotel.entity.Room;
import com.Kodakdev.KodakHotel.exception.BusinessException;
import com.Kodakdev.KodakHotel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class IRoomServiceImpl implements IRoomService {

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private BookingDao bookingDao;

    @Autowired
    private AwsS3Service awsS3Service;

    @Override
    public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal price, String description) {
        Response response = new Response();

        try {
            // we don't have aws access. so we hardCoded the value
//            String imageUrl = awsS3Service.saveImageToS3(photo);
            String imageUrl = "https://kodak-hotel-images/" + photo;
            Room room = new Room();
            room.setRoomPhotoUrl(imageUrl);
            room.setRoomType(roomType);
            room.setDescription(description);
            room.setRoomPrice(price);

            Room savedRoom = roomDao.saveAndFlush(room);

            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDto(savedRoom);

            response.setRoom(roomDTO);
            response.setStatusCode(200);
            response.setMessage("SUCCESSFUL");

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room" + e.getMessage());
        }

        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomDao.findDistinctRoomTypes();
    }

    @Override
    public Response getAllRoom() {
        Response response = new Response();

        try {
            List<Room> rooms = roomDao.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDto(rooms);

            response.setRoomList(roomDTOList);
            response.setStatusCode(200);
            response.setMessage("SUCCESSFUL");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Failed to get Room " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteRoom(Long roomId) {
        Response response = new Response();
        if(roomId == null) {
            throw new BusinessException("Object not found");
        }
        try {
            roomDao.deleteById(roomId);
            response.setMessage("SUCCESSFUL");
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error while delete room " + e.getMessage());
        }

        return  response;
    }

    @Override
    public Response updateRoom(Long roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo) {
        Response response = new Response();

        try {
            String imageUrl = null;
            if(photo != null && !photo.isEmpty()) {
//                imageUrl = awsS3Service.saveImageToS3(photo);
            }
//            imageUrl = awsS3Service.saveImageToS3(photo);

            imageUrl = "https://kodak-hotel-images/" + photo;

            Room room = roomDao.findById(roomId).orElseThrow(()-> new BusinessException("Room not found"));
            if(roomType != null) room.setRoomType(roomType);
            room.setRoomPhotoUrl(imageUrl);
            room.setRoomType(roomType);
            room.setDescription(description);
            room.setRoomPrice(roomPrice);

            Room savedRoom = roomDao.saveAndFlush(room);

            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDto(savedRoom);

            response.setRoom(roomDTO);
            response.setStatusCode(200);
            response.setMessage("SUCCESSFUL");

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getRoomById(Long roomId) {
        Response response = new Response();

        if(roomId == null) {
            throw new BusinessException("Object not found");
        }
        try {
            Room room = roomDao.findById(roomId).orElseThrow(()-> new BusinessException("Room not found"));
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDto(room);

            response.setStatusCode(200);
            response.setMessage("SUCCESSFUL");
            response.setRoom(roomDTO);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error while getting room");
        }

        return response;
    }

    @Override
    public Response getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        Response response = new Response();

        try {
            List<Room> roomList = roomDao.findAvailableRoomsByDatesAndTypes(checkInDate, checkOutDate, roomType);
            List<RoomDTO> roomDTOs = Utils.mapRoomListEntityToRoomListDto(roomList);

            response.setStatusCode(200);
            response.setMessage("SUCCESSFUL");
            response.setRoomList(roomDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error while getting room");
        }

        return response;
    }

    @Override
    public Response getAvailableRooms() {
        Response response = new Response();

        try {
            List<Room> roomList = roomDao.getAvailableRooms();
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDto(roomList);

            response.setStatusCode(200);
            response.setMessage("SUCCESSFUL");
            response.setRoomList(roomDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error while getting room");
        }

        return response;
    }
}
