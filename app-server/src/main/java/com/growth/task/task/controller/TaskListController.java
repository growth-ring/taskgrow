package com.growth.task.task.controller;

import com.growth.task.task.dto.TaskListRequest;
import com.growth.task.task.dto.TaskListResponse;
import com.growth.task.task.service.TaskListService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Task", description = "Task API Document")
public class TaskListController {
    private final TaskListService taskListService;

    public TaskListController(TaskListService taskListService) {
        this.taskListService = taskListService;
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<TaskListResponse> getList(@ModelAttribute @Valid TaskListRequest taskListRequest) {

        return taskListService.getTasks(taskListRequest);
    }
}
