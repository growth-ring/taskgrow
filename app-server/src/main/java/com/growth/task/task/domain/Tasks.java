package com.growth.task.task.domain;

import com.growth.task.global.domain.BaseTimeEntity;
import com.growth.task.user.domain.Users;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "task_date"}))
public class Tasks extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
            referencedColumnName = "user_id",
            foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT)
    )
    private Users user;
    @Column(name = "task_date", nullable = false)
    private LocalDate taskDate;

    @Builder
    public Tasks(long taskId, Users user, LocalDate taskDate) {
        this.taskId = taskId;
        this.user = user;
        this.taskDate = taskDate;
    }
}
