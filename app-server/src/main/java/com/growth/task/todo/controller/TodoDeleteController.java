package com.growth.task.todo.controller;

import com.growth.task.todo.application.TodoDeleteService;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoDeleteController {

    private final TodoDeleteService todoDeleteService;

    public TodoDeleteController(TodoDeleteService todoDeleteService) {
        this.todoDeleteService = todoDeleteService;
    }

    @DeleteMapping("/{todo_id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("todo_id") Long todoId) {
        todoDeleteService.deleteByTodoId(todoId);
    }
}
