package com.Kodakdev.KodakHotel.service;

import com.Kodakdev.KodakHotel.dao.BookingDao;
import com.Kodakdev.KodakHotel.dao.RoomDao;
import com.Kodakdev.KodakHotel.dao.UserDao;
import com.Kodakdev.KodakHotel.dto.BookingDTO;
import com.Kodakdev.KodakHotel.dto.Response;
import com.Kodakdev.KodakHotel.entity.Booking;
import com.Kodakdev.KodakHotel.entity.Room;
import com.Kodakdev.KodakHotel.entity.User;
import com.Kodakdev.KodakHotel.exception.BusinessException;
import com.Kodakdev.KodakHotel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService{

    @Autowired
    private BookingDao bookingDao;

    @Autowired
    private IRoomService roomService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoomDao roomDao;


    @Override
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {
        Response response = new Response();

        try {
            if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
                throw new IllegalArgumentException("Check in date must come after checkout date");
            }
            Room room = roomDao.findById(roomId).orElseThrow(() -> new BusinessException("Room not found"));
            User user = userDao.findById(userId).orElseThrow(() -> new BusinessException("User not found"));

            List<Booking> existingBookings = room.getBookings();
            if(!roomIsAvailable(bookingRequest, existingBookings)) {
                throw new BusinessException("Room not available for selected range");
            }

            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);

            String confirmationCode = Utils.generateConfirmationCode(10);
            bookingRequest.setBookingConfirmationCode(confirmationCode);

            bookingDao.saveAndFlush(bookingRequest);

            response.setStatusCode(200);
            response.setMessage("SUCCESSFUL");
            response.setBookingConfirmationCode(confirmationCode);
        } catch (BusinessException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(500);
        }

        return response;
    }

    @Override
    public Response findByBookingByConfirmationCode(String confirmationCode) {
        Response response = new Response();

        try {
            Booking booking = bookingDao.findByBookingConfirmationCode(confirmationCode).orElseThrow(() -> new BusinessException("Booking not found"));
            BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDto(booking);
            response.setStatusCode(200);
            response.setMessage("SUCCESSFUL");
            response.setBooking(bookingDTO);
        } catch (BusinessException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(500);
        }

        return response;
    }

    @Override
    public Response getAllBookings() {
        Response response = new Response();

        try {
            List<Booking> bookingList = bookingDao.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDto(bookingList);

            response.setStatusCode(200);
            response.setMessage("SUCCESSFUL");
            response.setBookingList(bookingDTOList);
        } catch (BusinessException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setMessage("Error Findings booking " + e.getMessage());
            response.setStatusCode(500);
        }

        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        Response response = new Response();

        try {
            Booking bookings = bookingDao.findById(bookingId).orElseThrow(() -> new BusinessException("Booking does not exist"));
            bookingDao.delete(bookings);

            response.setStatusCode(200);
            response.setMessage("SUCCESSFUL");
        } catch (BusinessException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setMessage("Error Findings booking " + e.getMessage());
            response.setStatusCode(500);
        }

        return response;
    }

    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate())
                        || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
                        && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())));

        // need to update it at time 7:11:30;
    }
}
