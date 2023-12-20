package com.growth.task.review.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@Getter
public class ReviewStatsResponse {
    private Map<Integer, Long> feelings;

    public ReviewStatsResponse(Map<Integer, Long> feelings) {
        this.feelings = feelings;
    }
}
