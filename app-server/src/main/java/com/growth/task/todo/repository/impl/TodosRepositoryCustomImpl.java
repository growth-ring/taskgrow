package com.growth.task.todo.repository.impl;

import com.growth.task.task.dto.TaskTodoDetailResponse;
import com.growth.task.todo.enums.Status;
import com.growth.task.todo.repository.TodosRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.growth.task.pomodoro.domain.QPomodoros.pomodoros;
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
                .where(todos.status.ne(Status.DONE))
                .limit(limit)
                .fetch()
                ;
    }
}
