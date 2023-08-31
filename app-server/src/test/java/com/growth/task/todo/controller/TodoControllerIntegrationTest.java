package com.growth.task.todo.controller;

import com.growth.task.todo.application.TodosService;
import com.growth.task.todo.dto.response.TodoGetResponse;
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
class TodoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodosService todosService;

    @Test
    public void getTodosTest() throws Exception {
        TodoGetResponse todo1 = new TodoGetResponse(1L, 1L, "디자인패턴의 아름다움 스터디", Status.READY, 1, 2);
        TodoGetResponse todo2 = new TodoGetResponse(2L, 1L, "프로젝트 진행하기", Status.PROGRESS, 1, 2);
        List<TodoGetResponse> todoList = Arrays.asList(todo1, todo2);
        when(todosService.getTodosByTaskId(1L)).thenReturn(todoList);

        mockMvc.perform(get("/api/v1/todos/").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].todoId").value(1L))
                .andExpect(jsonPath("$[0].taskId").value(1L))
                .andExpect(jsonPath("$[0].todo").value("디자인패턴의 아름다움 스터디"))
                .andExpect(jsonPath("$[0].status").value("READY"))
                .andExpect(jsonPath("$[0].performCount").value(1))
                .andExpect(jsonPath("$[0].planCount").value(2))
                .andExpect(jsonPath("$[1].todoId").value(2L))
                .andExpect(jsonPath("$[1].taskId").value(1L))
                .andExpect(jsonPath("$[1].todo").value("프로젝트 진행하기"))
                .andExpect(jsonPath("$[1].status").value("PROGRESS"))
                .andExpect(jsonPath("$[1].performCount").value(1))
                .andExpect(jsonPath("$[1].planCount").value(2));
    }
}
