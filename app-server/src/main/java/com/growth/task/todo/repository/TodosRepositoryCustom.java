package com.growth.task.todo.repository;

import com.growth.task.task.dto.TaskTodoDetailResponse;
import com.growth.task.todo.dto.TodoResponse;
import com.growth.task.todo.dto.response.TodoDetailResponse;
import com.growth.task.todo.dto.response.TodoWithPomodoroResponse;
import com.growth.task.todo.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TodosRepositoryCustom {
    List<TaskTodoDetailResponse> getTaskTodoPreview(Long taskId, int limit);

    List<TodoResponse> findByUserIdAndBetweenTimeRange(Long userId, LocalDate startDate, LocalDate endDate);

    List<TodoWithPomodoroResponse> findTodoWithPomodoroByTaskId(Long taskId);

    Page<TodoDetailResponse> findAllByUserAndParams(Pageable pageable, Long userId, Status status, LocalDate startDate, LocalDate endDate);
}
