package com.growth.task.review.domain;

import com.growth.task.commons.domain.BaseTimeEntity;
import com.growth.task.task.domain.Tasks;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * 회고
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", referencedColumnName = "task_id")
    private Tasks tasks;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;
    @Column(name = "feelings_score", nullable = false)
    private Integer feelingsScore;

    @Builder
    public Review(Tasks tasks, String contents, Integer feelingsScore) {
        this.tasks = tasks;
        this.contents = contents;
        this.feelingsScore = feelingsScore;
    }
}
