package com.growth.task.todo.repository;

import com.growth.task.task.dto.TaskTodoDetailResponse;

import java.util.List;

public interface TodosRepositoryCustom {
    List<TaskTodoDetailResponse> getTaskTodoPreview(Long taskId, int limit);
}
