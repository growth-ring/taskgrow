package com.growth.task.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growth.task.pomodoro.dto.request.PomodoroAddRequest;
import com.growth.task.todo.application.TodoAddService;
import com.growth.task.todo.dto.composite.TodoAndPomodoroAddRequest;
import com.growth.task.todo.dto.composite.TodoAndPomodoroAddResponse;
import com.growth.task.todo.dto.request.TodoAddRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoAddController.class)
class TodoAddControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoAddService todoAddService;

    @BeforeEach
    public void setUp() {
        TodoAndPomodoroAddResponse response = TodoAndPomodoroAddResponse.builder()
                .todoId(1L)
                .taskId(0L)
                .todo("스터디 하기")
                .status("대기중")
                .performCount(2)
                .planCount(5)
                .build();

        when(todoAddService.save(Mockito.any(TodoAndPomodoroAddRequest.class))).thenReturn(response);
    }

    @Test
    public void testCreateTodoAndPomodoro() throws Exception {
        // 요청 데이터 생성
        TodoAddRequest todoAddRequest = TodoAddRequest.builder()
                .taskId(0L)
                .todo("디자인패턴의 아름다움 스터디")
                .build();
        PomodoroAddRequest pomodoroAddRequest = PomodoroAddRequest.builder()
                .performCount(2)
                .planCount(5)
                .build();
        TodoAndPomodoroAddRequest request = TodoAndPomodoroAddRequest.builder()
                .todoAddRequest(todoAddRequest)
                .pomodoroAddRequest(pomodoroAddRequest)
                .build();


        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.todo_id").value(1L))
                .andExpect(jsonPath("$.task_id").value(0L))
                .andExpect(jsonPath("$.todo").value("스터디 하기"))
                .andExpect(jsonPath("$.status").value("대기중"))
                .andExpect(jsonPath("$.perform_count").value(2))
                .andExpect(jsonPath("$.plan_count").value(5));
    }
}
