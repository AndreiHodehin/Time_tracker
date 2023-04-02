package com.example.trecking_time.utils;

import com.example.trecking_time.entity.User;
import com.example.trecking_time.entity.dto.Activity;
import com.example.trecking_time.entity.ActivityEntity;
import com.example.trecking_time.entity.dto.UserDto;
import com.example.trecking_time.enums.Role;
import com.example.trecking_time.repository.UserRepository;
import com.example.trecking_time.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Converter {

    private final UserRepository userRepository;
    public ActivityEntity toEntity(Activity activity) {
        ActivityEntity entity = new ActivityEntity();
        entity.setId(activity.getId());
        entity.setName(activity.getName());
        entity.setDay(activity.getDay());
        entity.setStartTime(activity.getStartTime());
        entity.setEndTime(activity.getEndTime());
        entity.setExpectationDuration(activity.getExpectationDuration());
        entity.setInAction(activity.isInAction());
        entity.setUser(userRepository.getReferenceById(activity.getUserId()));
        return entity;
    }
    public Activity toDto(ActivityEntity entity) {
        return new Activity(
                entity.getId(),
                entity.getName(),
                entity.getDay(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getExpectationDuration(),
                entity.isInAction(),
                entity.getUser().getId()
        );
    }

    public UserDto userToDto(User user) {
        return new UserDto(user.getId(), user.getUsername());
    }
    public User userToEntity(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.getRole().add(Role.USER);
        return user;
    }
}
