package com.growth.task.review.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ReviewUpdateRequest {
    @NotBlank(message = "오늘의 한 줄을 입력해주세요.")
    private String subject;
    @NotBlank(message = "회고 내용을 입력해주세요.")
    private String contents;
    @Min(value = 1, message = "기분점수는 1~10 사이로 입력해주세요.")
    @Max(value = 10, message = "기분점수는 1~10 사이로 입력해주세요.")
    private Integer feelingsScore;

    public ReviewUpdateRequest(
            String subject,
            String contents,
            Integer feelingsScore
    ) {
        this.subject = subject;
        this.contents = contents;
        this.feelingsScore = feelingsScore;
    }
}
