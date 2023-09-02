package com.growth.task.task.service;

import com.growth.task.task.domain.Tasks;
import com.growth.task.task.dto.TaskDetailResponse;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.application.TodosService;
import com.growth.task.todo.exception.TaskNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskDetailService {
    private final TasksRepository tasksRepository;
    private final TodosService todosService;

    public TaskDetailService(TasksRepository tasksRepository, TodosService todosService) {
        this.tasksRepository = tasksRepository;
        this.todosService = todosService;
    }

    public TaskDetailResponse getTask(Long taskId) {
        Tasks task = tasksRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        List<String> todos = todosService.getTodosTop3ByTaskId(task.getTaskId());

        return new TaskDetailResponse(task.getTaskId(), todos);
    }
}
