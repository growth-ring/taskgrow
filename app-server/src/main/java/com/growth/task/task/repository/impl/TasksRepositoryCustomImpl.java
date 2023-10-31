package com.growth.task.task.repository.impl;

import com.growth.task.task.dto.TaskListRequest;
import com.growth.task.task.dto.TaskListQueryResponse;
import com.growth.task.task.repository.TasksRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.growth.task.review.domain.QReview.review;
import static com.growth.task.task.domain.QTasks.tasks;
import static com.growth.task.todo.domain.QTodos.todos;

@Repository
public class TasksRepositoryCustomImpl implements TasksRepositoryCustom {
    public static final int NOT_EXIST_SCORE = -1;
    private final JPAQueryFactory queryFactory;

    public TasksRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<TaskListQueryResponse> findRemainedTodosByUserBetweenTimeRange(TaskListRequest request) {
        Long userId = request.getUserId();
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        return queryFactory.select(Projections.fields(TaskListQueryResponse.class,
                        tasks.taskId,
                        tasks.user.userId.as("userId"),
                        tasks.taskDate,
                        todos.status.as("todoStatus"),
                        review.feelingsScore.coalesce(NOT_EXIST_SCORE).as("feelingsScore")
                ))
                .from(tasks)
                .leftJoin(todos)
                .on(tasks.taskId.eq(todos.task.taskId))
                .leftJoin(review)
                .on(tasks.taskId.eq(review.tasks.taskId))
                .where(
                        tasks.user.userId.eq(userId)
                                .and(tasks.taskDate.between(startDate, endDate))
                )
                .fetch()
                ;
    }
}
