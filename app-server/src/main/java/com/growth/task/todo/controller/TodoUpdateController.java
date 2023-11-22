package com.growth.task.todo.controller;

import com.growth.task.todo.application.TodoUpdateService;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.request.TodoUpdateRequest;
import com.growth.task.todo.dto.response.TodoUpdateResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/todos")
@Tag(name = "Todo", description = "Todo API Document")
public class TodoUpdateController {

    private final TodoUpdateService todoUpdateService;

    public TodoUpdateController(TodoUpdateService todoUpdateService) {
        this.todoUpdateService = todoUpdateService;
    }

    @PatchMapping("/{todo_id}")
    @ResponseStatus(OK)
    public TodoUpdateResponse update(
            @PathVariable("todo_id") Long todoId,
            @RequestBody @Valid TodoUpdateRequest todoUpdateRequest
    ) {
        Todos todos = todoUpdateService.update(todoId, todoUpdateRequest);
        return new TodoUpdateResponse(todoId, todos);
    }
}
