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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TodoCategoryUpdateServiceTest {
    private TodoCategoryUpdateService todoCategoryUpdateService;

    @Mock
    private TodoCategoryRepository todoCategoryRepository;
    @Mock
    private CategoryRetrieveService categoryRetrieveService;

    @BeforeEach
    void setUp() {
        todoCategoryUpdateService = new TodoCategoryUpdateService(todoCategoryRepository, categoryRetrieveService);
    }

    @Nested
    @DisplayName("update")
    class Describe_update {
        @Nested
        @DisplayName("category가 있는 todo와 새로운 카테고리가 주어지면")
        class Context_with_todo_when_with_category {
            private Todos givenTodo = Todos.builder()
                    .todoId(1L)
                    .orderNo(1)
                    .status(Status.READY)
                    .todo("엘라스틱 스택 8 읽기")
                    .task(mock(Tasks.class))
                    .build();
            private Category givenCategory = Category.builder()
                    .name("공부")
                    .build();
            private Category newCategory = Category.builder()
                    .name("독서")
                    .build();
            private TodoCategory todoCategory = new TodoCategory(givenTodo, givenCategory);

            @BeforeEach
            void prepare() {
                ReflectionTestUtils.setField(givenCategory, "id", 1L);
                ReflectionTestUtils.setField(newCategory, "id", 2L);

                given(categoryRetrieveService.getCategory(2L))
                        .willReturn(newCategory);
                given(todoCategoryRepository.findByTodos(givenTodo))
                        .willReturn(Optional.of(todoCategory));
            }

            @Test
            @DisplayName("새로운 카테고리로 수정된다")
            void it_update_category() {
                todoCategoryUpdateService.update(givenTodo, newCategory.getId());

                assertAll(
                        () -> assertThat(todoCategory.getCategory().getName()).isEqualTo(newCategory.getName()),
                        () -> verify(todoCategoryRepository, times(0)).save(any())
                );
            }
        }

        @Nested
        @DisplayName("category가 없는 todo와 새로운 카테고리가 주어지면")
        class Context_with_todo_when_without_category {
            private Todos givenTodo = Todos.builder()
                    .todoId(1L)
                    .orderNo(1)
                    .status(Status.READY)
                    .todo("엘라스틱 스택 8 읽기")
                    .task(mock(Tasks.class))
                    .build();
            private Category givenCategory = Category.builder()
                    .name("공부")
                    .build();
            private TodoCategory todoCategory = new TodoCategory(givenTodo, givenCategory);

            @BeforeEach
            void prepare() {
                ReflectionTestUtils.setField(givenCategory, "id", 1L);

                given(categoryRetrieveService.getCategory(1L))
                        .willReturn(givenCategory);
                given(todoCategoryRepository.save(any(TodoCategory.class)))
                        .willReturn(todoCategory);
            }

            @Test
            @DisplayName("새로운 카테고리로 수정된다")
            void it_update_category() {
                todoCategoryUpdateService.update(givenTodo, givenCategory.getId());

                assertAll(
                        () -> verify(todoCategoryRepository, times(1)).save(any())
                );
            }
        }
    }
}
