package com.Kodakdev.KodakHotel.utils;

import com.Kodakdev.KodakHotel.dto.BookingDTO;
import com.Kodakdev.KodakHotel.dto.RoomDTO;
import com.Kodakdev.KodakHotel.dto.UserDTO;
import com.Kodakdev.KodakHotel.entity.Booking;
import com.Kodakdev.KodakHotel.entity.Room;
import com.Kodakdev.KodakHotel.entity.User;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static final SecureRandom secureRandom = new SecureRandom();

    public static String generateConfirmationCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<length;i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }

    public static UserDTO mapUserEntityToUserDto(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setEmail(user.getUsername());
        userDTO.setName(user.getName());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());

        return userDTO;
    }

    public static UserDTO mapUserEntityToUserDtoWithBookings(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setEmail(user.getUsername());
        userDTO.setName(user.getName());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());

        if(!user.getBookings().isEmpty()) {
            userDTO.setBookings(user.getBookings().stream().map(booking -> mapBookingEntityToBookingDtoWithBookedRooms(booking, false)).collect(Collectors.toList()));
        }

        return userDTO;
    }

    public static UserDTO mapUserEntityToUserDTOWithUserBookingsAndRoom(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setEmail(user.getUsername());
        userDTO.setName(user.getName());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());

        if(!user.getBookings().isEmpty()) {
            userDTO.setBookings(user.getBookings().stream().map(booking -> mapBookingEntityToBookingDtoWithBookedRooms(booking, false)).collect(Collectors.toList()));
        }

        return userDTO;
    }

    public static RoomDTO mapRoomEntityToRoomDto(Room room) {
        RoomDTO roomDTO = new RoomDTO();

        roomDTO.setId(room.getId());
        roomDTO.setRoomType(room.getRoomType());
        roomDTO.setRoomPrice(room.getRoomPrice());
        roomDTO.setRoomPhotoUrl(room.getRoomPhotoUrl());
        roomDTO.setDescription(room.getDescription());

        return roomDTO;
    }

    public static RoomDTO mapRoomEntityToRoomDtoWithBookings(Room room) {
        RoomDTO roomDTO = new RoomDTO();

        roomDTO.setId(room.getId());
        roomDTO.setRoomType(room.getRoomType());
        roomDTO.setRoomPrice(room.getRoomPrice());
        roomDTO.setRoomPhotoUrl(room.getRoomPhotoUrl());
        roomDTO.setDescription(room.getDescription());

        if(!room.getBookings().isEmpty()) {
            roomDTO.setBookings(room.getBookings().stream().map(Utils::mapBookingEntityToBookingDto).collect(Collectors.toList()));
        }

        return roomDTO;
    }

    public static BookingDTO mapBookingEntityToBookingDto(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();

        bookingDTO.setId(booking.getId());
        bookingDTO.setCheckInDate(booking.getCheckInDate());
        bookingDTO.setCheckOutDate(booking.getCheckOutDate());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());
        bookingDTO.setNumOfAdults(booking.getNumOfAdults());
        bookingDTO.setNumOfChildren(booking.getNumOfChildren());
        bookingDTO.setTotalNumOfGuest(booking.getTotalNumOfGuest());

        return bookingDTO;
    }

    public static BookingDTO mapBookingEntityToBookingDtoWithBookedRooms(Booking booking, boolean mapUser) {
        BookingDTO bookingDTO = new BookingDTO();

        bookingDTO.setId(booking.getId());
        bookingDTO.setCheckInDate(booking.getCheckInDate());
        bookingDTO.setCheckOutDate(booking.getCheckOutDate());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());
        bookingDTO.setNumOfAdults(booking.getNumOfAdults());
        bookingDTO.setNumOfChildren(booking.getNumOfChildren());
        bookingDTO.setTotalNumOfGuest(booking.getTotalNumOfGuest());

        if(mapUser) {
            bookingDTO.setUser(Utils.mapUserEntityToUserDto(booking.getUser()));
        }

        if(booking.getRoom() != null) {
            bookingDTO.setRoom(Utils.mapRoomEntityToRoomDto(booking.getRoom()));
        }

        return bookingDTO;
    }

    public static List<UserDTO> mapUserListEntityToUserLustDto(List<User> userList) {
        return userList.stream().map(Utils::mapUserEntityToUserDto).collect(Collectors.toList());
    }

    public static List<RoomDTO> mapRoomListEntityToRoomListDto(List<Room> roomList) {
        return roomList.stream().map(Utils::mapRoomEntityToRoomDto).collect(Collectors.toList());
    }

    public static List<BookingDTO> mapBookingListEntityToBookingListDto(List<Booking> bookingList) {
        return bookingList.stream().map(Utils::mapBookingEntityToBookingDto).collect(Collectors.toList());
    }
}
