package com.growth.task.todo.domain;

import com.growth.task.commons.domain.BaseTimeEntity;
import com.growth.task.task.domain.Tasks;
import com.growth.task.todo.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import static com.growth.task.todo.enums.Status.PROGRESS;
import static com.growth.task.todo.enums.Status.isDone;
import static com.growth.task.todo.enums.Status.isReady;
import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
public class Todos extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long todoId;

    @ManyToOne(fetch = LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "task_id",
            referencedColumnName = "task_id",
            foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT)
    )
    private Tasks task;

    @Column(name = "todo", length = 18, nullable = false)
    private String todo;

    @Column(name = "status", columnDefinition = "varchar(20) default 'READY'", nullable = false)
    private Status status;

    @Builder
    public Todos(Long todoId, Tasks task, String todo, Status status) {
        this.todoId = todoId;
        this.task = task;
        this.todo = todo;
        this.status = status != null ? status : Status.READY;
    }

    protected Todos() {
    }

    public void updateTodo(String todo) {
        this.todo = todo;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public void validateCompletePomodoro() {
        if (isReady(this.status)) {
            this.updateStatus(PROGRESS);
        } else if (isDone(this.status)) {
            throw new IllegalArgumentException(String.format("이미 완료된 Todo입니다. Todo=%s", this.todo));
        }
    }

    public void validateUpdatePomodoro() {
        if (!isReady(status)) {
            throw new IllegalArgumentException(String.format("변경할 수 없는 상태입니다. status= ", status));
        }
    }
}
