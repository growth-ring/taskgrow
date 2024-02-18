package com.growth.task.category.service;

import com.growth.task.category.domain.Category;
import com.growth.task.category.dto.CategoryResponse;
import com.growth.task.category.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CategoryRetrieveServiceTest {
    private CategoryRetrieveService categoryRetrieveService;
    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRetrieveService = new CategoryRetrieveService(categoryRepository);
    }

    @Nested
    @DisplayName("getCategories")
    class Describe_getCategories {
        @Nested
        @DisplayName("카테고리가 존재한다면")
        class Context_exist_categories {
            Category givenCategoryStudy = Category.builder().name("공부").build();
            Category givenCategoryRead = Category.builder().name("독서").build();
            Category givenCategoryWork = Category.builder().name("일").build();

            @BeforeEach
            void prepare() {
                given(categoryRepository.findAll())
                        .willReturn(List.of(
                                givenCategoryStudy,
                                givenCategoryRead,
                                givenCategoryWork
                        ));
            }

            @Test
            @DisplayName("존재하는 카테고리들을 응답한다")
            void it_response_categories() {
                List<CategoryResponse> categories = categoryRetrieveService.getCategories();

                assertThat(categories).hasSize(3);
            }
        }
    }
}
