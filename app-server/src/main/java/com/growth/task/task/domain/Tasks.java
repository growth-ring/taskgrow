package com.growth.task.task.domain;

import com.growth.task.commons.domain.BaseTimeEntity;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
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
    private LocalDateTime taskDate;

    @Builder
    public Tasks(long taskId, Users user, LocalDateTime taskDate) {
        this.taskId = taskId;
        this.user = user;
        this.taskDate = taskDate;
    }
}
