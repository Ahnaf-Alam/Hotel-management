package com.Kodakdev.KodakHotel.service;

import com.Kodakdev.KodakHotel.dao.UserDao;
import com.Kodakdev.KodakHotel.dto.LoginRequest;
import com.Kodakdev.KodakHotel.dto.Response;
import com.Kodakdev.KodakHotel.dto.UserDTO;
import com.Kodakdev.KodakHotel.entity.User;
import com.Kodakdev.KodakHotel.exception.BusinessException;
import com.Kodakdev.KodakHotel.utils.JWTUtils;
import com.Kodakdev.KodakHotel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IUserServiceImpl implements IUserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    public static final String DEFAULT_ROLE = "USER";

    @Override
    public Response register(User user) {
        Response response = new Response();
        User toReturn = null;

        if(user == null) {
            throw new BusinessException("User Not found");
        }
        try {
            if(user.getRole() == null || user.getRole().isBlank()) {
                user.setRole(DEFAULT_ROLE);
            }

            boolean existingUser = userDao.existsByEmail(user.getUsername());

            if(existingUser) {
                throw  new BusinessException(user.getUsername() + " already exists");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            toReturn = userDao.saveAndFlush(user);

            UserDTO userDTO = Utils.mapUserEntityToUserDto(toReturn);
            response.setStatusCode(200);
            response.setUserDTO(userDTO);
        } catch (BusinessException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred during user registration " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        Response response = new Response();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            User user = userDao.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new BusinessException("User not found"));

            var jwt = jwtUtils.generateToken(user);

            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setExpirationTime("7 Days");
            response.setMessage("SUCCESSFUL");
        } catch (BusinessException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred during user login " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllUsers() {
        Response response = new Response();

        try {
            List<User> userList = userDao.findAll();
            List<UserDTO> userDTOList = Utils.mapUserListEntityToUserLustDto(userList);

            response.setStatusCode(200);
            response.setUserList(userDTOList);
        } catch (BusinessException e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @Override
    public Response getUserBookingHistory(String userId) {
        Response response = new Response();

        try {
            User user = userDao.findById(Long.valueOf(userId)).orElseThrow(() -> new BusinessException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTOWithUserBookingsAndRoom(user);

            response.setStatusCode(200);
            response.setMessage("SUCCESSFUL");
            response.setUserDTO(userDTO);
        } catch (BusinessException e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred during user login " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response deleteUser(String userId) {
        Response response = new Response();

        try {
            userDao.findById(Long.valueOf(userId)).orElseThrow(() -> new BusinessException("User Not Found"));
            userDao.deleteById(Long.valueOf(userId));

            response.setStatusCode(200);
            response.setMessage("SUCCESSFUL");
        } catch (BusinessException e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred during user login " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getUserById(String userId) {
        Response response = new Response();

        try {
            User user = userDao.findById(Long.valueOf(userId)).orElseThrow(() -> new BusinessException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDto(user);

            response.setStatusCode(200);
            response.setMessage("SUCCESSFUL");
            response.setUserDTO(userDTO);
        } catch (BusinessException e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred during user login " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getUserInfo(String email) {
        Response response = new Response();

        try {
            User user = userDao.findByEmail(email).orElseThrow(() -> new BusinessException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDto(user);

            response.setStatusCode(200);
            response.setMessage("SUCCESSFUL");
            response.setUserDTO(userDTO);
        } catch (BusinessException e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred during user login " + e.getMessage());
        }

        return response;
    }
}
