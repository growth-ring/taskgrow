package com.growth.task.task.repository;

import com.growth.task.task.dto.TaskListRequest;
import com.growth.task.task.dto.TaskListQueryResponse;

import java.util.List;

public interface TasksRepositoryCustom {
    List<TaskListQueryResponse> findRemainedTodosByUserBetweenTimeRange(TaskListRequest request);
}
