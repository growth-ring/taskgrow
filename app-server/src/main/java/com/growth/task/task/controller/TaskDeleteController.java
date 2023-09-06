package com.growth.task.task.controller;

import com.growth.task.task.service.TaskDeleteService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskDeleteController {
    private final TaskDeleteService taskDeleteService;

    public TaskDeleteController(TaskDeleteService taskDeleteService) {
        this.taskDeleteService = taskDeleteService;
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("taskId") Long taskId) {
        taskDeleteService.deleteByTaskId(taskId);
    }
}
