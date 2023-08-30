package com.growth.task.task.repository.impl;

import com.growth.task.task.dto.TaskListResponse;
import com.growth.task.task.repository.TaskRepositoryCustom;
import com.growth.task.todo.enums.Status;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.growth.task.task.domain.QTasks.tasks;
import static com.growth.task.todo.domain.QTodos.todos;

@Repository
public class TaskRepositoryImpl implements TaskRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public TaskRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<TaskListResponse> findRemainedTodosByUserBetweenTimeRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return queryFactory.select(Projections.fields(TaskListResponse.class,
                        tasks.taskId,
                        tasks.user.userId.as("userId"),
                        tasks.taskDate,
                        todos.todoId.count().as("todos")
                ))
                .from(tasks)
                .leftJoin(todos)
                .on(tasks.taskId.eq(todos.task.taskId))
                .where(todos.status.ne(Status.DONE))
                .groupBy(tasks.taskId)
                .fetch()
                ;
    }
}
