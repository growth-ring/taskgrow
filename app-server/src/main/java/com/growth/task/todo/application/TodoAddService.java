package com.growth.task.todo.application;

import com.growth.task.task.domain.Tasks;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.request.TodoAddRequest;
import com.growth.task.todo.exception.TaskNotFoundException;
import com.growth.task.todo.repository.TodosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoAddService {
    private TodosRepository todosRepository;

    private TasksRepository tasksRepository;

    public TodoAddService(TodosRepository todosRepository, TasksRepository tasksRepository) {
        this.todosRepository = todosRepository;
        this.tasksRepository = tasksRepository;
    }

    @Transactional
    public Todos save(TodoAddRequest todoAddRequest) {
        Long taskId = todoAddRequest.getTaskId();
        Tasks tasks = tasksRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        Todos todos = todoAddRequest.toEntity(tasks);
        return todosRepository.save(todos);
    }
}
