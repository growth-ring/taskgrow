package com.growth.task.todo.application;

import com.growth.task.task.domain.Tasks;
import com.growth.task.task.repository.TasksRepository;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.request.TodoAddRequest;
import com.growth.task.todo.dto.request.TodoUpdateRequest;
import com.growth.task.todo.exception.TaskNotFoundException;
import com.growth.task.todo.exception.TodoNotFoundException;
import com.growth.task.todo.repository.TodosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TodoService {
    private TodosRepository todosRepository;

    private TasksRepository tasksRepository;

    public TodoService(TodosRepository todosRepository, TasksRepository tasksRepository) {
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

    @Transactional
    public Todos update(Long todoId, TodoUpdateRequest todoUpdateRequest) {
        Todos todos = todosRepository.findById(todoId)
                .orElseThrow(() -> new TodoNotFoundException(todoId));

        if (todoUpdateRequest.getTodo() != null) {
            todos.updateTodo(todoUpdateRequest.getTodo());
        }
        if (todoUpdateRequest.getStatus() != null) {
            todos.updateStatus(todoUpdateRequest.getStatus());
        }
        todosRepository.save(todos);

        return todos;
    }
}
