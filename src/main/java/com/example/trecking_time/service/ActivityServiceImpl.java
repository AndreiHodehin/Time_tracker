package com.example.trecking_time.service;

import com.example.trecking_time.entity.dto.Activity;
import com.example.trecking_time.entity.ActivityEntity;
import com.example.trecking_time.repository.ActivityRepository;
import com.example.trecking_time.service.interfaces.ActivityService;
import com.example.trecking_time.utils.Converter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository repository;
    private final Converter converter;

    @Override
    public void addActivity(Activity activity) {
        ActivityEntity entity = converter.toEntity(activity);
        repository.save(entity);
        activity.setId(entity.getId());
    }

    @Override
    public void updateActivity(Activity activity) {
        repository.saveAndFlush(converter.toEntity(activity));
    }

    @Override
    public void deleteActivity(Activity activity) {
        repository.delete(converter.toEntity(activity));
    }

    @Override
    public Activity findActivityById(Long id) {
        ActivityEntity entity = repository.findById(id).orElseThrow();
        return converter.toDto(entity);
    }

    @Override
    public List<Activity> findAllActivity() {
        return repository.findAll().stream().map(converter::toDto).collect(Collectors.toList());
    }

    @Override
    public List<Activity> findAllActivityByDay(LocalDate date) {
        return repository.findAllByDay(date)
                .stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Activity> findAllActivityByDayAndUserId(LocalDate date, Long id) {
        return repository.findAllByDayAndUser(date,id)
                .stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
    }
}
