package com.growth.task.todo.controller;

import com.growth.task.todo.application.TodoListService;
import com.growth.task.todo.dto.response.TodoWithPomodoroResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/todos")
@Tag(name = "Todo", description = "Todo API Document")
public class TodoListController {

    private final TodoListService todoListService;

    public TodoListController(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TodoWithPomodoroResponse> getTodos(@RequestParam(name = "task_id", required = true) Long taskId) {
        return todoListService.getTodosByTaskId(taskId);
    }
}
