package com.growth.task.mypage.controller;

import com.growth.task.review.dto.ReviewStatsRequest;
import com.growth.task.review.dto.ReviewStatsResponse;
import com.growth.task.review.service.ReviewListService;
import com.growth.task.todo.application.TodoListService;
import com.growth.task.todo.dto.TodoListRequest;
import com.growth.task.todo.dto.TodoStatsRequest;
import com.growth.task.todo.dto.TodoStatsResponse;
import com.growth.task.todo.dto.response.TodoDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mypage/{user_id}")
@Tag(name = "MyPage", description = "My Page API Document")
public class MyPageController {
    private final TodoListService todoListService;
    private final ReviewListService reviewListService;

    public MyPageController(
            TodoListService todoListService,
            ReviewListService reviewListService
    ) {
        this.todoListService = todoListService;
        this.reviewListService = reviewListService;
    }

    @Operation(summary = "사용자 Todo 통계")
    @GetMapping("/todos/stats")
    @ResponseStatus(HttpStatus.OK)
    public TodoStatsResponse getTodoCounts(
            @PathVariable("user_id") Long userId,
            @ModelAttribute @Valid TodoStatsRequest request
    ) {
        return todoListService.getTodoStats(userId, request);
    }

    @Operation(summary = "사용자 Todo 통계 세부 리스트")
    @GetMapping("/todos")
    @ResponseStatus(HttpStatus.OK)
    public Page<TodoDetailResponse> getTodosByUser(
            @PathVariable("user_id") Long userId,
            @PageableDefault(size = 10) Pageable pageable,
            @ModelAttribute TodoListRequest request
    ) {
        return todoListService.getTodoByUserAndParams(pageable, userId, request);
    }
    @Operation(summary = "사용자 회고 통계")
    @GetMapping("/review/stats")
    @ResponseStatus(HttpStatus.OK)
    public ReviewStatsResponse getReviewStats(
            @PathVariable("user_id") Long userId,
            @ModelAttribute @Valid ReviewStatsRequest request
    ){
        return reviewListService.getReviewStats(userId, request);
    }
}
