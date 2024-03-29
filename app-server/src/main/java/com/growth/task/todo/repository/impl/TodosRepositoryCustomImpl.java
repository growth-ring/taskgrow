package com.growth.task.todo.repository.impl;

import com.growth.task.task.dto.TaskTodoDetailResponse;
import com.growth.task.todo.dto.TodoResponse;
import com.growth.task.todo.dto.response.TodoDetailResponse;
import com.growth.task.todo.dto.response.TodoWithPomodoroResponse;
import com.growth.task.todo.enums.Status;
import com.growth.task.todo.repository.TodosRepositoryCustom;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.growth.task.category.domain.QCategory.category;
import static com.growth.task.pomodoro.domain.QPomodoros.pomodoros;
import static com.growth.task.task.domain.QTasks.tasks;
import static com.growth.task.todo.domain.QTodoCategory.todoCategory;
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
    public List<TodoResponse> findByUserIdAndBetweenTimeRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return queryFactory.select(Projections.fields(TodoResponse.class,
                        todos.todoId,
                        todos.task.taskId,
                        todos.todo,
                        todos.status,
                        todos.orderNo
                ))
                .from(todos)
                .leftJoin(tasks)
                .on(todos.task.eq(tasks))
                .where(
                        eqUserId(userId),
                        betweenTimeRange(startDate, endDate)
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
                        todos.orderNo,
                        pomodoros.performCount,
                        pomodoros.planCount,
                        category.name.as("category")
                ))
                .from(todos)
                .leftJoin(pomodoros)
                .on(pomodoros.todo.eq(todos))
                .leftJoin(todoCategory)
                .on(todoCategory.todos.eq(todos))
                .leftJoin(category)
                .on(todoCategory.category.eq(category))
                .where(eqTaskId(taskId))
                .fetch()
                ;
    }

    @Override
    public Page<TodoDetailResponse> findAllByUserAndParams(Pageable pageable, Long userId, Status status, LocalDate startDate, LocalDate endDate) {

        List<TodoDetailResponse> content = queryFactory
                .select(Projections.fields(TodoDetailResponse.class,
                        todos.todo,
                        todos.status,
                        todos.orderNo,
                        pomodoros.performCount,
                        pomodoros.planCount,
                        tasks.taskDate
                ))
                .from(todos)
                .leftJoin(tasks)
                .on(tasks.taskId.eq(todos.task.taskId))
                .leftJoin(pomodoros)
                .on(pomodoros.todo.eq(todos))
                .where(
                        eqUserId(userId),
                        eqStatus(status),
                        betweenTimeRange(startDate, endDate)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(tasks.taskDate.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(todos.count())
                .from(todos)
                .leftJoin(tasks)
                .on(tasks.taskId.eq(todos.task.taskId))
                .leftJoin(pomodoros)
                .on(pomodoros.todo.eq(todos))
                .where(
                        eqUserId(userId),
                        eqStatus(status),
                        betweenTimeRange(startDate, endDate)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private Predicate eqStatus(Status status) {
        if (status == null) {
            return null;
        }
        return todos.status.eq(status);
    }

    private static BooleanExpression eqUserId(Long userId) {
        return tasks.user.userId.eq(userId);

    }

    private static BooleanExpression betweenTimeRange(LocalDate startDate, LocalDate endDate) {
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
