package com.growth.task.todo.repository;

import com.growth.task.task.dto.TaskTodoDetailResponse;
import com.growth.task.todo.dto.TodoStatsRequest;
import com.growth.task.todo.dto.TodoResponse;

import java.util.List;

public interface TodosRepositoryCustom {
    List<TaskTodoDetailResponse> getTaskTodoPreview(Long taskId, int limit);

    List<TodoResponse> findByUserIdAndBetweenTimeRange(Long userId, TodoStatsRequest request);
}
