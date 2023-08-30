package com.growth.task.task.repository;

import com.growth.task.task.dto.TaskListResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepositoryCustom {
    List<TaskListResponse> findRemainedTodosByUserBetweenTimeRange(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
