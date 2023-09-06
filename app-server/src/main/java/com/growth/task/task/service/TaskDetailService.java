package com.growth.task.task.service;

import com.growth.task.task.domain.Tasks;
import com.growth.task.task.dto.TaskDetailResponse;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.application.TodoListService;
import com.growth.task.todo.exception.TaskNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskDetailService {
    private final TasksRepository tasksRepository;
    private final TodoListService todoListService;

    public TaskDetailService(TasksRepository tasksRepository, TodoListService todoListService) {
        this.tasksRepository = tasksRepository;
        this.todoListService = todoListService;
    }

    public TaskDetailResponse getTask(Long taskId) {
        Tasks task = tasksRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        List<String> todos = todoListService.getTodosTop3ByTaskId(task.getTaskId());

        return new TaskDetailResponse(task.getTaskId(), todos);
    }
}
