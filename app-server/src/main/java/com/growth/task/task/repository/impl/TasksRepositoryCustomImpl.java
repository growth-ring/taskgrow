package com.growth.task.task.repository.impl;

import com.growth.task.task.dto.TaskListWithTodoStatusResponse;
import com.growth.task.task.repository.TasksRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.growth.task.task.domain.QTasks.tasks;
import static com.growth.task.todo.domain.QTodos.todos;

@Repository
public class TasksRepositoryCustomImpl implements TasksRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public TasksRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<TaskListWithTodoStatusResponse> findRemainedTodosByUserBetweenTimeRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return queryFactory.select(Projections.fields(TaskListWithTodoStatusResponse.class,
                        tasks.taskId,
                        tasks.user.userId.as("userId"),
                        tasks.taskDate,
                        todos.status.as("todoStatus")
                ))
                .from(tasks)
                .leftJoin(todos)
                .on(tasks.taskId.eq(todos.task.taskId))
                .where(
                        tasks.user.userId.eq(userId)
                                .and(tasks.taskDate.between(startDate, endDate))
                )
                .groupBy(tasks.taskId)
                .fetch()
                ;
    }
}
