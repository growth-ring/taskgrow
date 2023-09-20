package com.growth.task.todo.controller;

import com.growth.task.todo.application.TodoService;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.request.TodoUpdateRequest;
import com.growth.task.todo.dto.response.TodoUpdateResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoUpdateController {

    private final TodoService todoService;

    public TodoUpdateController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PatchMapping("/{todo_id}")
    @ResponseStatus(OK)
    public TodoUpdateResponse update(
            @PathVariable("todo_id") Long todoId,
            @RequestBody @Valid TodoUpdateRequest todoUpdateRequest
    ) {
        Todos todos = todoService.update(todoId, todoUpdateRequest);
        return new TodoUpdateResponse(todoId, todos);
    }
}
