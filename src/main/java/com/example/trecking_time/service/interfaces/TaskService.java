package com.example.trecking_time.service.interfaces;

import com.example.trecking_time.entity.Task;

import java.util.List;

public interface TaskService {
    Task createTask(Task task);
    void updateTask(Task task);
    void deleteTaskByName(String name);
    Task findTaskById(Long id);
    List<Task> findAllTasks();
}
