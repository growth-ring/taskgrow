package com.growth.task.task.domain;

import com.growth.task.user.domain.Users;
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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private long taskId;

    @ManyToOne
    @JoinColumn(name = "user_id",
            referencedColumnName = "user_id",
            foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT)
    )
    private Users userId;
    @Column(name = "task_date", nullable = false)
    private LocalDateTime taskDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Tasks(long taskId, Users userId, LocalDateTime taskDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.taskId = taskId;
        this.userId = userId;
        this.taskDate = taskDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    protected Tasks() {
    }
}
