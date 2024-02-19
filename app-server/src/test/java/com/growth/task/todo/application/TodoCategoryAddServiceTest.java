package com.growth.task.todo.application;

import com.growth.task.category.domain.Category;
import com.growth.task.category.service.CategoryRetrieveService;
import com.growth.task.task.domain.Tasks;
import com.growth.task.todo.domain.TodoCategory;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.enums.Status;
import com.growth.task.todo.repository.TodoCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TodoCategoryAddServiceTest {
    private TodoCategoryAddService todoCategoryAddService;

    @Mock
    private TodoCategoryRepository todoCategoryRepository;
    @Mock
    private CategoryRetrieveService categoryRetrieveService;

    @BeforeEach
    void setUp() {
        todoCategoryAddService = new TodoCategoryAddService(todoCategoryRepository, categoryRetrieveService);
    }

    @Nested
    @DisplayName("save")
    class Describe_save {
        @Nested
        @DisplayName("투두와 카테고리 id가 주어지면")
        class Context_with_todo_and_category {
            private Todos givenTodo = Todos.builder()
                    .task(mock(Tasks.class))
                    .todo("인간관계론 읽기")
                    .status(Status.READY)
                    .orderNo(1)
                    .build();
            private Category category = Category.builder()
                    .name("독서하기")
                    .build();

            @BeforeEach
            void prepare() {
                ReflectionTestUtils.setField(category, "id", 1L);

                given(categoryRetrieveService.getCategory(1L))
                        .willReturn(category);
                given(todoCategoryRepository.save(any()))
                        .willReturn(new TodoCategory(givenTodo, category));
            }

            @Test
            @DisplayName("투두 카테고리 연관 관계를 저장한다")
            void it_save() {
                TodoCategory actual = todoCategoryAddService.save(givenTodo, category.getId());

                assertAll(
                        () -> assertThat(actual.getTodos().getTodo()).isEqualTo(givenTodo.getTodo()),
                        () -> assertThat(actual.getTodos().getStatus()).isEqualTo(givenTodo.getStatus()),
                        () -> assertThat(actual.getTodos().getOrderNo()).isEqualTo(givenTodo.getOrderNo()),
                        () -> assertThat(actual.getCategory().getName()).isEqualTo(category.getName()),
                        () -> verify(categoryRetrieveService, times(1)).getCategory(any())
                );
            }
        }

        @Nested
        @DisplayName("카테고리 id가 주어지지 않으면")
        class Context_without_category_id {
            private Todos givenTodo = Todos.builder()
                    .task(mock(Tasks.class))
                    .todo("인간관계론 읽기")
                    .status(Status.READY)
                    .orderNo(1)
                    .build();

            @Test
            @DisplayName("아무 일도 하지 않고 null을 리턴한다")
            void it_nothing_and_return_null() {
                TodoCategory actual = todoCategoryAddService.save(givenTodo, null);

                assertAll(
                        () -> assertThat(actual).isNull(),
                        () -> verify(categoryRetrieveService, times(0)).getCategory(any())
                );
            }
        }
    }
}
