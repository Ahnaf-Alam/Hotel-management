package com.Kodakdev.KodakHotel.service;

import com.Kodakdev.KodakHotel.dto.Response;
import com.Kodakdev.KodakHotel.entity.Booking;

public interface BookingService {
    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);
    Response findByBookingByConfirmationCode(String confirmationCode);
    Response getAllBookings();
    Response cancelBooking(Long bookingId);
}
