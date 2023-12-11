package com.growth.task.todo.repository;

import com.growth.task.task.dto.TaskTodoDetailResponse;
import com.growth.task.todo.dto.TodoListRequest;
import com.growth.task.todo.dto.TodoResponse;
import com.growth.task.todo.dto.TodoStatsRequest;
import com.growth.task.todo.dto.response.TodoDetailResponse;
import com.growth.task.todo.dto.response.TodoWithPomodoroResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TodosRepositoryCustom {
    List<TaskTodoDetailResponse> getTaskTodoPreview(Long taskId, int limit);

    List<TodoResponse> findByUserIdAndBetweenTimeRange(Long userId, TodoStatsRequest request);

    List<TodoWithPomodoroResponse> findTodoWithPomodoroByTaskId(Long taskId);

    Page<TodoDetailResponse> findAllByUserAndParams(Pageable pageable, Long userId, TodoListRequest request);
}
