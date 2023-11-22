package com.growth.task.todo.application;

import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.request.TodoUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoUpdateService {
    private final TodoDetailService todoDetailService;

    public TodoUpdateService(TodoDetailService todoDetailService) {
        this.todoDetailService = todoDetailService;
    }

    @Transactional
    public Todos update(Long todoId, TodoUpdateRequest todoUpdateRequest) {
        Todos todos = todoDetailService.getTodo(todoId);

        if (todoUpdateRequest.hasTodo()) {
            todos.updateTodo(todoUpdateRequest.getTodo());
        }
        if (todoUpdateRequest.hasStatus()) {
            todos.updateStatus(todoUpdateRequest.getStatus());
        }

        return todos;
    }
}
