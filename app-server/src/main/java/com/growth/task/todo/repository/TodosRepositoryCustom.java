package com.growth.task.todo.repository;

import com.growth.task.task.dto.TaskTodoDetailResponse;
import com.growth.task.todo.dto.TodoStatsRequest;
import com.growth.task.todo.dto.TodoResponse;
import com.growth.task.todo.dto.response.TodoWithPomodoroResponse;

import java.util.List;

public interface TodosRepositoryCustom {
    List<TaskTodoDetailResponse> getTaskTodoPreview(Long taskId, int limit);

    List<TodoResponse> findByUserIdAndBetweenTimeRange(Long userId, TodoStatsRequest request);

    List<TodoWithPomodoroResponse> findTodoWithPomodoroByTaskId(Long taskId);
}
