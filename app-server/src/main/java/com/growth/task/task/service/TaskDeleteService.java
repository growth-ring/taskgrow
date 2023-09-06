package com.growth.task.task.service;

import com.growth.task.task.domain.Tasks;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.exception.TaskNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TaskDeleteService {
    private final TasksRepository tasksRepository;

    public TaskDeleteService(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    public void deleteByTaskId(Long taskId) {
        Tasks tasks = tasksRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        tasksRepository.delete(tasks);
    }
}
