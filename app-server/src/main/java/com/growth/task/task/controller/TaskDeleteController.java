package com.growth.task.task.controller;

import com.growth.task.task.service.TaskDeleteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Task", description = "Task API Document")
public class TaskDeleteController {
    private final TaskDeleteService taskDeleteService;

    public TaskDeleteController(TaskDeleteService taskDeleteService) {
        this.taskDeleteService = taskDeleteService;
    }

    @DeleteMapping("/{task_id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("task_id") Long taskId) {
        taskDeleteService.deleteByTaskId(taskId);
    }
}
