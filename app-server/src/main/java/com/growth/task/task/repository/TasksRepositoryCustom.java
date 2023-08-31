package com.growth.task.task.repository;

import com.growth.task.task.dto.TaskListWithTodoStatusResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface TasksRepositoryCustom {
    List<TaskListWithTodoStatusResponse> findRemainedTodosByUserBetweenTimeRange(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
