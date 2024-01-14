package com.growth.task.review.domain;

import com.growth.task.global.domain.BaseTimeEntity;
import com.growth.task.review.dto.ReviewUpdateRequest;
import com.growth.task.review.exception.OutOfBoundsException;
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
    public static final int FEELING_SCORE_LOWER_BOUND = 1;
    public static final int FEELING_SCORE_UPPER_BOUND = 10;
    public static final String FEELING_SCORE_OUT_OF_BOUNDS_MESSAGE = "기분 점수는 1과 10 사이어야 합니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;
    @OnDelete(action = OnDeleteAction.CASCADE)

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", referencedColumnName = "task_id")
    private Tasks tasks;
    @Column(length = 50, nullable = false)
    private String subject;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;
    @Column(name = "feelings_score", nullable = false)
    private Integer feelingsScore;

    @Builder
    public Review(
            Tasks tasks,
            String subject,
            String contents,
            Integer feelingsScore
    ) {
        validFeelingsScore(feelingsScore);
        this.tasks = tasks;
        this.subject = subject;
        this.contents = contents;
        this.feelingsScore = feelingsScore;
    }

    public void update(ReviewUpdateRequest request) {
        validFeelingsScore(request.getFeelingsScore());
        this.subject = request.getSubject();
        this.feelingsScore = request.getFeelingsScore();
        this.contents = request.getContents();
    }

    private void validFeelingsScore(Integer feelingsScore) {
        if (!isBetween(feelingsScore, FEELING_SCORE_LOWER_BOUND, FEELING_SCORE_UPPER_BOUND)) {
            throw new OutOfBoundsException(FEELING_SCORE_OUT_OF_BOUNDS_MESSAGE);
        }
    }

    private static boolean isBetween(Integer value, int lower, int upper) {
        return value >= lower && value <= upper;
    }
}
