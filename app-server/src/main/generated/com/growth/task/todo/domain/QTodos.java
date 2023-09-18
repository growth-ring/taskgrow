package com.growth.task.todo.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTodos is a Querydsl query type for Todos
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTodos extends EntityPathBase<Todos> {

    private static final long serialVersionUID = -1493160964L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTodos todos = new QTodos("todos");

    public final com.growth.task.commons.domain.QBaseTimeEntity _super = new com.growth.task.commons.domain.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<com.growth.task.todo.enums.Status> status = createEnum("status", com.growth.task.todo.enums.Status.class);

    public final com.growth.task.task.domain.QTasks task;

    public final StringPath todo = createString("todo");

    public final NumberPath<Long> todoId = createNumber("todoId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QTodos(String variable) {
        this(Todos.class, forVariable(variable), INITS);
    }

    public QTodos(Path<? extends Todos> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTodos(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTodos(PathMetadata metadata, PathInits inits) {
        this(Todos.class, metadata, inits);
    }

    public QTodos(Class<? extends Todos> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.task = inits.isInitialized("task") ? new com.growth.task.task.domain.QTasks(forProperty("task"), inits.get("task")) : null;
    }

}

