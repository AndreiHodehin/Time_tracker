package com.example.trecking_time.service;

import com.example.trecking_time.entity.Task;
import com.example.trecking_time.repository.TaskRepository;
import com.example.trecking_time.service.interfaces.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;
    @Override
    public Task createTask(Task task) {
        return repository.save(task);
    }

    @Override
    public void updateTask(Task task) {
        repository.saveAndFlush(task);
    }

    @Override
    public void deleteTask(Task task) {
        repository.delete(task);
    }

    @Override
    public Task findTaskById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public List<Task> findAllTasks() {
        return repository.findAll();
    }
}
