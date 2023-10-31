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
    @NotNull(message = "테스크 id는 필수입니다.")
    private Long taskId;
    @NotBlank(message = "회고 내용은 필수입니다.")
    private String contents;
    @Min(value = 1, message = "기분 점수는 1 ~ 10 사이를 입력해주세요.")
    @Max(value = 10, message = "기분 점수는 1 ~ 10 사이를 입력해주세요.")
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
