package com.growth.task.todo.controller;

import com.growth.task.todo.application.TodoListService;
import com.growth.task.todo.dto.TodoListRequest;
import com.growth.task.todo.dto.TodoStatsRequest;
import com.growth.task.todo.dto.TodoStatsResponse;
import com.growth.task.todo.dto.response.TodoDetailResponse;
import com.growth.task.todo.dto.response.TodoWithPomodoroResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/stats/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public TodoStatsResponse getTodoCounts(
            @PathVariable("user_id") Long userId,
            @ModelAttribute @Valid TodoStatsRequest request
    ) {
        return todoListService.getTodoStats(userId, request);
    }

    @GetMapping("/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public Page<TodoDetailResponse> getTodosByUser(
            @PathVariable("user_id") Long userId,
            @PageableDefault(size = 10) Pageable pageable,
            @ModelAttribute TodoListRequest request
    ) {
        return todoListService.getTodoByUserAndParams(pageable, userId, request);
    }
}
