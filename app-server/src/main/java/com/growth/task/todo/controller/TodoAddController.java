package com.growth.task.todo.controller;

import com.growth.task.todo.application.TodoWithRelatedAddService;
import com.growth.task.todo.dto.composite.TodoAndPomodoroAddRequest;
import com.growth.task.todo.dto.composite.TodoAndPomodoroAddResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin
@RestController
@RequestMapping("/api/v1/todos")
@Tag(name = "Todo", description = "Todo API Document")
public class TodoAddController {

    private final TodoWithRelatedAddService todoWithRelatedAddService;

    public TodoAddController(TodoWithRelatedAddService todoWithRelatedAddService) {
        this.todoWithRelatedAddService = todoWithRelatedAddService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoAndPomodoroAddResponse create(@RequestBody @Valid TodoAndPomodoroAddRequest todoAndPomodoroAddRequest) {
        return todoWithRelatedAddService.saveWithRelated(todoAndPomodoroAddRequest);
    }
}
