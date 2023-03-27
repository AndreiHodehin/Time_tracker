package com.example.trecking_time.utils;

import com.example.trecking_time.entity.Activity;
import com.example.trecking_time.entity.ActivityEntity;
import org.springframework.stereotype.Component;

@Component
public class Converter {

    public ActivityEntity toEntity(Activity activity) {
        ActivityEntity entity = new ActivityEntity();
        entity.setId(activity.getId());
        entity.setName(activity.getName());
        entity.setDay(activity.getDay());
        entity.setStartTime(activity.getStartTime());
        entity.setEndTime(activity.getEndTime());
        entity.setExpectationDuration(activity.getExpectationDuration());
        entity.setInAction(activity.isInAction());
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
                entity.isInAction()
        );
    }
}
