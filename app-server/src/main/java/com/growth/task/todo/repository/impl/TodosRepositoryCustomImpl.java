package com.growth.task.todo.repository.impl;

import com.growth.task.task.dto.TaskTodoDetailResponse;
import com.growth.task.todo.dto.TodoResponse;
import com.growth.task.todo.dto.TodoStatsRequest;
import com.growth.task.todo.dto.response.TodoWithPomodoroResponse;

import com.growth.task.todo.enums.Status;
import com.growth.task.todo.repository.TodosRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.growth.task.pomodoro.domain.QPomodoros.pomodoros;
import static com.growth.task.task.domain.QTasks.tasks;
import static com.growth.task.todo.domain.QTodos.todos;

@Repository
public class TodosRepositoryCustomImpl implements TodosRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public TodosRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<TaskTodoDetailResponse> getTaskTodoPreview(Long taskId, int limit) {
        return queryFactory.select(Projections.fields(TaskTodoDetailResponse.class,
                        todos.todo,
                        pomodoros.performCount,
                        pomodoros.planCount
                ))
                .from(todos)
                .innerJoin(pomodoros)
                .on(todos.todoId.eq(pomodoros.todo.todoId))
                .fetchJoin()
                .where(
                        eqTaskId(taskId),
                        neStatusDone()
                )
                .limit(limit)
                .fetch()
                ;
    }

    @Override
    public List<TodoResponse> findByUserIdAndBetweenTimeRange(Long userId, TodoStatsRequest request) {
        return queryFactory.select(Projections.fields(TodoResponse.class,
                        todos.todoId,
                        todos.task.taskId,
                        todos.todo,
                        todos.status
                ))
                .from(todos)
                .leftJoin(tasks)
                .on(todos.task.eq(tasks))
                .where(
                        eqUserId(userId),
                        betweenTimeRange(request)
                )
                .fetch()
                ;
    }

    @Override
    public List<TodoWithPomodoroResponse> findTodoWithPomodoroByTaskId(Long taskId) {
        return queryFactory.select(Projections.fields(TodoWithPomodoroResponse.class,
                        todos.todoId,
                        todos.task.taskId,
                        todos.todo,
                        todos.status,
                        pomodoros.performCount,
                        pomodoros.planCount
                ))
                .from(todos)
                .leftJoin(pomodoros)
                .on(pomodoros.todo.eq(todos))
                .fetch()
                ;

    private static BooleanExpression eqUserId(Long userId) {
        return tasks.user.userId.eq(userId);

    }

    private static BooleanExpression betweenTimeRange(TodoStatsRequest request) {
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();
        if (startDate == null && endDate == null) {
            return null;
        }
        return tasks.taskDate.between(startDate, endDate);
    }

    private static BooleanExpression neStatusDone() {
        return todos.status.ne(Status.DONE);
    }

    private static BooleanExpression eqTaskId(Long taskId) {
        return todos.task.taskId.eq(taskId);
    }
}
