package com.growth.task.task.controller;

import com.growth.task.task.dto.TaskDetailResponse;
import com.growth.task.task.service.TaskDetailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Task", description = "Task API Document")
public class TaskDetailController {
    private final TaskDetailService taskDetailService;

    public TaskDetailController(TaskDetailService taskDetailService) {
        this.taskDetailService = taskDetailService;
    }

    @GetMapping("/{task_id}/todos")
    @ResponseStatus(OK)
    public TaskDetailResponse getTask(@PathVariable("task_id") Long taskId) {
        return taskDetailService.getTask(taskId);
    }
}
