package com.growth.task.todo.controller;

import com.growth.task.todo.application.TodoListService;
import com.growth.task.todo.dto.response.TodoListResponse;
import com.growth.task.todo.enums.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TodoListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoListService todoListService;

    @Test
    public void getTodosTest() throws Exception {
        TodoListResponse todo1 = new TodoListResponse(1L, 1L, "디자인패턴의 아름다움 스터디", Status.READY, 1, 2);
        TodoListResponse todo2 = new TodoListResponse(2L, 1L, "프로젝트 진행하기", Status.PROGRESS, 1, 2);
        List<TodoListResponse> todoList = Arrays.asList(todo1, todo2);
        when(todoListService.getTodosByTaskId(1L)).thenReturn(todoList);

        mockMvc.perform(get("/api/v1/todos").param("task_id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].todo_id").value(1L))
                .andExpect(jsonPath("$[0].task_id").value(1L))
                .andExpect(jsonPath("$[0].todo").value("디자인패턴의 아름다움 스터디"))
                .andExpect(jsonPath("$[0].status").value("READY"))
                .andExpect(jsonPath("$[0].perform_count").value(1))
                .andExpect(jsonPath("$[0].plan_count").value(2))
                .andExpect(jsonPath("$[1].todo_id").value(2L))
                .andExpect(jsonPath("$[1].task_id").value(1L))
                .andExpect(jsonPath("$[1].todo").value("프로젝트 진행하기"))
                .andExpect(jsonPath("$[1].status").value("PROGRESS"))
                .andExpect(jsonPath("$[1].perform_count").value(1))
                .andExpect(jsonPath("$[1].plan_count").value(2));
    }
}
