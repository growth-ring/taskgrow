package com.growth.task.pomodoro.domain;

import com.growth.task.global.domain.BaseTimeEntity;
import com.growth.task.todo.domain.Todos;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@DynamicUpdate
public class Pomodoros extends BaseTimeEntity {
    public static final int PLAN_COUNT_LIMIT = 20;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pomodoro_id")
    private long pomodoroId;

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "todo_id",
            referencedColumnName = "todo_id",
            foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT)
    )
    private Todos todo;

    @Column(name = "perform_count", nullable = false)
    private int performCount;

    @Column(name = "plan_count", nullable = false)
    private int planCount;

    @Builder
    public Pomodoros(long pomodoroId, Todos todo, int performCount, int planCount) {
        validate(planCount);
        this.pomodoroId = pomodoroId;
        this.todo = todo;
        this.performCount = performCount;
        this.planCount = planCount;
    }

    protected Pomodoros() {
    }

    public void increasePerformCount() {
        this.performCount++;
    }

    public void updatePerformCount(int performCount) {
        this.performCount = performCount;
    }

    public void updatePlanCount(int planCount) {
        this.planCount = planCount;
    }
    private void validate(int planCount){
        if(planCount > PLAN_COUNT_LIMIT){
            throw new IllegalArgumentException("plan_count는 20을 넘을 수 없습니다.");
        }
    }
}
