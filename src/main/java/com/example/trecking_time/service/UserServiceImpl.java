package com.example.trecking_time.service;

import com.example.trecking_time.entity.User;
import com.example.trecking_time.entity.dto.UserDto;
import com.example.trecking_time.repository.UserRepository;
import com.example.trecking_time.service.interfaces.UserService;
import com.example.trecking_time.utils.Converter;
import com.vaadin.flow.spring.security.AuthenticationContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Converter converter;
    @Override
    public UserDto createUser(User user) {
        userRepository.save(user);
        return converter.userToDto(user);
    }

    @Override
    public UserDto getUserById(long id) {
        User user = userRepository.findById(id).orElseThrow();
        return converter.userToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(converter::userToDto).toList();
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public UserDto getUserByName(AuthenticationContext authContext) {
        String username = authContext.getAuthenticatedUser(org.springframework.security.core.userdetails.User.class)
                .orElseThrow()
                .getUsername();
        User user = userRepository.findByUsername(username);
        return converter.userToDto(user);
    }
}
