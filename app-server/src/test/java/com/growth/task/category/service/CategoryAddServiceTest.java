package com.growth.task.category.service;

import com.growth.task.category.domain.Category;
import com.growth.task.category.dto.CategoryRequest;
import com.growth.task.category.dto.CategoryResponse;
import com.growth.task.category.exception.CategoryAlreadyExistsException;
import com.growth.task.category.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryAddService")
class CategoryAddServiceTest {
    private static final String NAME = "공부";
    private CategoryAddService categoryAddService;
    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryAddService = new CategoryAddService(categoryRepository);
    }

    @DisplayName("save")
    @Nested
    class Describe_save {
        @DisplayName("올바른 정보가 주어지면")
        @Nested
        class Context_with_valid_data {
            private CategoryRequest request = CategoryRequest.builder()
                    .name(NAME)
                    .build();
            private Category givenCategory = Category.builder()
                    .name(NAME)
                    .build();

            @BeforeEach
            void prepare() {
                given(categoryRepository.findByName(any()))
                        .willReturn(Optional.empty());
                given(categoryRepository.save(any()))
                        .willReturn(givenCategory);
            }

            @DisplayName("저장 후 리턴한다")
            @Test
            void it_return_category() {
                CategoryResponse category = categoryAddService.save(request);

                assertThat(category.getName()).isEqualTo(NAME);
            }
        }

        @DisplayName("이미 존재하는 카테고리 명이 주어지면")
        @Nested
        class Context_with_already_existing_name {
            private CategoryRequest request = CategoryRequest.builder()
                    .name(NAME)
                    .build();
            private Category givenCategory = Category.builder()
                    .name(NAME)
                    .build();

            @BeforeEach
            void prepare() {
                given(categoryRepository.findByName(any()))
                        .willReturn(Optional.of(givenCategory));
            }

            @DisplayName("예외를 던진다")
            @Test
            void it_throw_exception() {
                assertThatThrownBy(() -> categoryAddService.save(request))
                        .isInstanceOf(CategoryAlreadyExistsException.class);
            }
        }
    }
}
