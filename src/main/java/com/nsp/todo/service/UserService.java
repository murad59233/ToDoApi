package com.nsp.todo.service;

import com.nsp.todo.model.dto.UserDto;
import com.nsp.todo.model.response.ResponseModel;
import com.nsp.todo.model.response.UserResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {
    ResponseModel<List<UserResponse>> getAllUsers();

    void updateUser(UserDto userDto, Long id);

    ResponseModel<UserResponse> getUserById(Long id);

    void deleteUser(Long id);

    void changeMail(String token);

    void deleteDisableUserByEmailOrUsername(String username, String email,boolean enabled);

    ResponseModel<UserResponse> getLoggedInUser(Authentication auth);
}
