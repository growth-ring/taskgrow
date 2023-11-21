package com.growth.task.todo.application;

import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.request.TodoUpdateRequest;
import com.growth.task.todo.exception.TodoNotFoundException;
import com.growth.task.todo.repository.TodosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoUpdateService {
    private final TodosRepository todosRepository;

    public TodoUpdateService(TodosRepository todosRepository) {
        this.todosRepository = todosRepository;
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
