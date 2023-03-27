package com.example.trecking_time.service.interfaces;

import com.example.trecking_time.entity.Activity;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ActivityService {

    Activity addActivity(Activity activity);
    void updateActivity(Activity activity);
    void deleteActivity(Activity activity);
    Activity findActivityById(Long id);
    List<Activity> findAllActivity();
}
