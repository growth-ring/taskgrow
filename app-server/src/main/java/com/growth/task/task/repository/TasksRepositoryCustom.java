package com.growth.task.task.repository;

import com.growth.task.task.dto.TaskListRequest;
import com.growth.task.task.dto.TaskListWithTodoStatusResponse;

import java.util.List;

public interface TasksRepositoryCustom {
    List<TaskListWithTodoStatusResponse> findRemainedTodosByUserBetweenTimeRange(TaskListRequest request);
}
