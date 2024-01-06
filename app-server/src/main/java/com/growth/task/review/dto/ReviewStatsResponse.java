package com.growth.task.review.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Getter
public class ReviewStatsResponse {
    private Map<Integer, Long> feelings;

    public ReviewStatsResponse(Map<Integer, Long> feelings) {
        this.feelings = feelings;
    }
}
