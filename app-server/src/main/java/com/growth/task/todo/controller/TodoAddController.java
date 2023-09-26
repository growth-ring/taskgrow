package com.growth.task.todo.controller;

import com.growth.task.todo.application.TodoAddService;
import com.growth.task.todo.dto.composite.TodoAndPomodoroAddRequest;
import com.growth.task.todo.dto.composite.TodoAndPomodoroAddResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/todos")
@Tag(name = "Todo", description = "Todo API Document")
public class TodoAddController {

    private final TodoAddService todoAddService;

    public TodoAddController(TodoAddService todoAddService) {
        this.todoAddService = todoAddService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoAndPomodoroAddResponse create(@RequestBody @Valid TodoAndPomodoroAddRequest todoAndPomodoroAddRequest) {
        return todoAddService.save(todoAndPomodoroAddRequest);
    }
}
