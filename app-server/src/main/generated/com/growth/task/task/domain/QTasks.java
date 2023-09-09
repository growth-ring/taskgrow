package com.growth.task.task.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTasks is a Querydsl query type for Tasks
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTasks extends EntityPathBase<Tasks> {

    private static final long serialVersionUID = 224655294L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTasks tasks = new QTasks("tasks");

    public final com.growth.task.commons.domain.QBaseTimeEntity _super = new com.growth.task.commons.domain.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> taskDate = createDateTime("taskDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> taskId = createNumber("taskId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.growth.task.user.domain.QUsers user;

    public QTasks(String variable) {
        this(Tasks.class, forVariable(variable), INITS);
    }

    public QTasks(Path<? extends Tasks> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTasks(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTasks(PathMetadata metadata, PathInits inits) {
        this(Tasks.class, metadata, inits);
    }

    public QTasks(Class<? extends Tasks> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.growth.task.user.domain.QUsers(forProperty("user")) : null;
    }

}

