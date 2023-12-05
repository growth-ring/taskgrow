package com.growth.task.review.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growth.task.review.domain.Review;
import com.growth.task.task.domain.Tasks;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class ReviewAddRequest {
    public static final String FEELINGS_SCORE_VALID_MESSAGE = "기분 점수는 1 ~ 10 사이를 입력해주세요.";
    public static final String REVIEW_ADD_REQUEST_TASK_ID_REQUIRED_MESSAGE = "테스크 id는 필수입니다.";
    public static final String REVIEW_ADD_REQUEST_CONTENTS_REQUIRED_MESSAGE = "회고 내용은 필수입니다.";
    @NotNull(message = REVIEW_ADD_REQUEST_TASK_ID_REQUIRED_MESSAGE)
    private Long taskId;
    @NotBlank(message = REVIEW_ADD_REQUEST_CONTENTS_REQUIRED_MESSAGE)
    private String contents;
    @Min(value = 1, message = FEELINGS_SCORE_VALID_MESSAGE)
    @Max(value = 10, message = FEELINGS_SCORE_VALID_MESSAGE)
    private Integer feelingsScore;

    public ReviewAddRequest(Long taskId, String contents, Integer feelingsScore) {
        this.taskId = taskId;
        this.contents = contents;
        this.feelingsScore = feelingsScore;
    }

    public Review toEntity(Tasks task) {
        return Review.builder()
                .tasks(task)
                .contents(contents)
                .feelingsScore(feelingsScore)
                .build();
    }
}
