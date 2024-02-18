package com.growth.task.category.dto;


import com.growth.task.category.domain.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryResponse {
    private Long id;
    private String name;

    @Builder
    public CategoryResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CategoryResponse of(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
