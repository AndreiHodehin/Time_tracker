package com.example.trecking_time.service.interfaces;

import com.example.trecking_time.entity.Activity;

import java.time.LocalDate;
import java.util.List;


public interface ActivityService {

    Activity addActivity(Activity activity);
    void updateActivity(Activity activity);
    void deleteActivity(Activity activity);
    Activity findActivityById(Long id);
    List<Activity> findAllActivity();
    List<Activity> findAllActivityByDay(LocalDate date);
}
