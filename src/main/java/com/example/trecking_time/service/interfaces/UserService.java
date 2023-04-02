package com.example.trecking_time.service.interfaces;

import com.example.trecking_time.entity.User;
import com.example.trecking_time.entity.dto.UserDto;
import com.vaadin.flow.spring.security.AuthenticationContext;

import java.util.List;

public interface UserService {
    UserDto createUser(User user);
    UserDto getUserById(long id);
    List<UserDto> getAllUsers();
    void deleteUser(User user);

    UserDto getUserByName(AuthenticationContext authContext);

}
