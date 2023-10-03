package com.growth.task.todo.controller;

import com.growth.task.todo.application.TodoService;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.request.TodoUpdateRequest;
import com.growth.task.todo.dto.response.TodoUpdateResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/todos")
@Tag(name = "Todo", description = "Todo API Document")
public class TodoUpdateController {

    private final TodoService todoService;

    public TodoUpdateController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PatchMapping("/{todoId}")
    @ResponseStatus(OK)
    public TodoUpdateResponse update(
            @PathVariable("todoId") Long todoId,
            @RequestBody @Valid TodoUpdateRequest todoUpdateRequest
    ) {
        Todos todos = todoService.update(todoId, todoUpdateRequest);
        return new TodoUpdateResponse(todos);
    }
}
