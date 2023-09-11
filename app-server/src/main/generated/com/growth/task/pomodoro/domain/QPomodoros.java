package com.growth.task.pomodoro.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPomodoros is a Querydsl query type for Pomodoros
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPomodoros extends EntityPathBase<Pomodoros> {

    private static final long serialVersionUID = 1867416694L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPomodoros pomodoros = new QPomodoros("pomodoros");

    public final com.growth.task.commons.domain.QBaseTimeEntity _super = new com.growth.task.commons.domain.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> performCount = createNumber("performCount", Integer.class);

    public final NumberPath<Integer> planCount = createNumber("planCount", Integer.class);

    public final NumberPath<Long> pomodoroId = createNumber("pomodoroId", Long.class);

    public final com.growth.task.todo.domain.QTodos todo;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPomodoros(String variable) {
        this(Pomodoros.class, forVariable(variable), INITS);
    }

    public QPomodoros(Path<? extends Pomodoros> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPomodoros(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPomodoros(PathMetadata metadata, PathInits inits) {
        this(Pomodoros.class, metadata, inits);
    }

    public QPomodoros(Class<? extends Pomodoros> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.todo = inits.isInitialized("todo") ? new com.growth.task.todo.domain.QTodos(forProperty("todo"), inits.get("todo")) : null;
    }

}

