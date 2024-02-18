package com.growth.task.category.dto;

import com.growth.task.category.domain.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Validated
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CategoryRequest {
    private static final String NAME_IS_REQUIRED_MESSAGE = "카테고리를 입력하세요.";
    @NotBlank(message = NAME_IS_REQUIRED_MESSAGE)
    private String name;

    @Builder
    public CategoryRequest(String name) {
        this.name = removeSpace(name);
    }

    public Category toEntity() {
        return Category.builder()
                .name(name)
                .build();
    }

    private String removeSpace(String name) {
        if (name != null) {
            return name.replace(" ", "");
        }
        return name;
    }
}
