package com.Kodakdev.KodakHotel.service;

import com.Kodakdev.KodakHotel.dto.LoginRequest;
import com.Kodakdev.KodakHotel.dto.Response;
import com.Kodakdev.KodakHotel.entity.User;

public interface IUserService {
    Response register(User user);
    Response login(LoginRequest loginRequest);
    Response getAllUsers();
    Response getUserBookingHistory(String userId);
    Response deleteUser(String userId);
    Response getUserById(String userId);
    Response getUserInfo(String email);
}
