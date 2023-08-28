package com.growth.task.task.controller;

import com.growth.task.task.dto.TaskAddRequest;
import com.growth.task.task.dto.TaskAddResponse;
import com.growth.task.task.service.TaskAddService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskAddController {
    private final TaskAddService taskAddService;

    public TaskAddController(TaskAddService taskAddService) {
        this.taskAddService = taskAddService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public TaskAddResponse create(@RequestBody @Valid TaskAddRequest taskAddRequest) {
        return taskAddService.save(taskAddRequest);
    }
}
