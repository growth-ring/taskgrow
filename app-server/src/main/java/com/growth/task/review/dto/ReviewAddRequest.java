package com.growth.task.review.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.growth.task.review.domain.Review;
import com.growth.task.task.domain.Tasks;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class ReviewAddRequest {
    private static final String FEELINGS_SCORE_VALID_MESSAGE = "기분 점수는 1 ~ 10 사이를 입력해주세요.";
    private static final String TASK_ID_REQUIRED_MESSAGE = "테스크 id는 필수입니다.";
    private static final String CONTENTS_REQUIRED_MESSAGE = "회고 내용은 필수입니다.";
    private static final String SUBJECT_REQUIRED_MESSAGE = "오늘의 한 줄은 필수입니다.";
    public static final String SUBJECT_MAX_LENGTH_MESSAGE = "오늘의 한 줄은 50자 내로 입력해주세요.";
    @NotNull(message = TASK_ID_REQUIRED_MESSAGE)
    private Long taskId;
    @NotBlank(message = SUBJECT_REQUIRED_MESSAGE)
    @Size(max = 50, message = SUBJECT_MAX_LENGTH_MESSAGE)
    private String subject;
    @NotBlank(message = CONTENTS_REQUIRED_MESSAGE)
    private String contents;
    @Min(value = 1, message = FEELINGS_SCORE_VALID_MESSAGE)
    @Max(value = 10, message = FEELINGS_SCORE_VALID_MESSAGE)
    private Integer feelingsScore;

    @Builder
    public ReviewAddRequest(
            Long taskId,
            String subject,
            String contents,
            Integer feelingsScore
    ) {
        this.taskId = taskId;
        this.subject = subject;
        this.contents = contents;
        this.feelingsScore = feelingsScore;
    }

    public Review toEntity(Tasks task) {
        return Review.builder()
                .tasks(task)
                .subject(subject)
                .contents(contents)
                .feelingsScore(feelingsScore)
                .build();
    }
}
