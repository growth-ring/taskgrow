package com.growth.task.todo.controller;

import com.growth.task.todo.application.TodosService;
import com.growth.task.todo.dto.composite.CompositeAddRequest;
import com.growth.task.todo.dto.composite.CompositeAddResponse;
import com.growth.task.todo.dto.response.TodoGetResponse;
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
    public List<TodoGetResponse> getTodos(@RequestParam(required = true) Long id) {
        return todosService.getTodosByTaskId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompositeAddResponse create(@RequestBody @Valid CompositeAddRequest compositeAddRequest) {
        return todosService.save(compositeAddRequest);
    }
}
