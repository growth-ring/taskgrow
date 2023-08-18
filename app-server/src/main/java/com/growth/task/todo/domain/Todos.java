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
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class Todos extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long todoId;

    @JoinColumn(name = "task_id",
            referencedColumnName = "task_id",
            foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT)
    )
    private Tasks task;

    @Column(name = "todo", nullable = false)
    private String todo;

    @Column(name = "status", nullable = false)
    private Status status;

    @Builder
    public Todos(Long todoId, Tasks task, String todo, Status status) {
        this.todoId = todoId;
        this.task = task;
        this.todo = todo;
        this.status = status;
    }

    protected Todos() {
    }
}
