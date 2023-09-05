package com.growth.task.todo.controller;

import com.growth.task.todo.application.TodosService;
import com.growth.task.todo.dto.composite.TodoAndPomodoroAddRequest;
import com.growth.task.todo.dto.composite.TodoAndPomodoroAddResponse;
import com.growth.task.todo.dto.response.TodoListResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {

    private final TodosService todosService;

    public TodoController(TodosService todosService) {
        this.todosService = todosService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TodoListResponse> getTodos(@RequestParam(required = true) Long taskId) {
        return todosService.getTodosByTaskId(taskId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoAndPomodoroAddResponse create(@RequestBody @Valid TodoAndPomodoroAddRequest todoAndPomodoroAddRequest) {
        return todosService.save(todoAndPomodoroAddRequest);
    }
}
