package com.growth.task.todo.controller;

import com.growth.task.todo.application.TodoUpdateService;
import com.growth.task.todo.dto.composite.TodoAndPomodoroUpdateRequest;
import com.growth.task.todo.dto.composite.TodoAndPomodoroUpdateResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoUpdateController {

    private final TodoUpdateService todoUpdateService;

    public TodoUpdateController(TodoUpdateService todoUpdateService) {
        this.todoUpdateService = todoUpdateService;
    }

    @PatchMapping("/{todoId}")
    @ResponseStatus(OK)
    public TodoAndPomodoroUpdateResponse update(
            @PathVariable("todoId") Long todoId,
            @RequestBody @Valid TodoAndPomodoroUpdateRequest todoAndPomodoroUpdateRequest
    ) {
        return todoUpdateService.update(todoId, todoAndPomodoroUpdateRequest);
    }
}
