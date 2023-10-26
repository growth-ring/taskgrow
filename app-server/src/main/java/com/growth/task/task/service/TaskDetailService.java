package com.growth.task.task.service;

import com.growth.task.task.domain.Tasks;
import com.growth.task.task.dto.TaskDetailResponse;
import com.growth.task.task.dto.TaskTodoDetailResponse;
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
        Tasks task = findTaskById(taskId);

        List<TaskTodoDetailResponse> todos = todoListService.getTaskTodosPreview(task.getTaskId());

        return new TaskDetailResponse(task.getTaskId(), todos);
    }

    public Tasks findTaskById(Long taskId) {
        return tasksRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
    }
}
