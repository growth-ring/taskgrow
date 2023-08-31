package com.growth.task.todo.controller;

import com.growth.task.todo.application.TodosService;
import com.growth.task.todo.dto.response.TodoGetResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {

    private final TodosService todosService;

    public TodoController(TodosService todosService) {
        this.todosService = todosService;
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TodoGetResponse>> getTodos(@RequestParam(required = true) Long id) {
        List<TodoGetResponse> todos = todosService.getTodosByTaskId(id);
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }
}
