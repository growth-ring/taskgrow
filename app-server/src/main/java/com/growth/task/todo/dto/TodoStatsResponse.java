package com.growth.task.todo.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

//  총 투두 개수, 완료한 투두 개수, 진행 중인 투두 개수, 미완료인 투두 개수
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TodoStatsResponse {
    private Long totalCount;
    private Long doneCount;
    private Long progressCount;
    private Long undoneCount;

    public TodoStatsResponse(
            Long totalCount,
            Long doneCount,
            Long progressCount,
            Long undoneCount
    ) {
        this.totalCount = totalCount;
        this.doneCount = doneCount;
        this.progressCount = progressCount;
        this.undoneCount = undoneCount;
    }
}
