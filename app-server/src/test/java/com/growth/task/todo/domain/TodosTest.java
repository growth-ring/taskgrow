package com.growth.task.todo.domain;

import com.growth.task.todo.enums.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Todo")
class TodosTest {
    @DisplayName("todo status 변경시, ready의 경우 완료로 변경 불가능하다")
    @Test
    void updateTodoWhenStatusReadyToDone() {
        Todos todo = Todos.builder()
                .status(Status.READY)
                .todo("테스트 하기")
                .todoId(1L)
                .build();

        assertThrows(IllegalArgumentException.class, () -> todo.updateStatus(Status.DONE));
    }
}
