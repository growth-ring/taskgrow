package com.growth.task.todo.application;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.dto.response.PomodoroAddResponse;
import com.growth.task.pomodoro.service.PomodoroAddService;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.composite.TodoAndPomodoroAddRequest;
import com.growth.task.todo.dto.composite.TodoAndPomodoroAddResponse;
import com.growth.task.todo.dto.response.TodoAddResponse;
import org.springframework.stereotype.Service;

@Service
public class TodoAddService {

    private final TodoService todoService;
    private final PomodoroAddService pomodoroAddService;

    public TodoAddService (
            TodoService todoService,
            PomodoroAddService pomodoroAddService
    ) {
        this.todoService = todoService;
        this.pomodoroAddService = pomodoroAddService;
    }

    public TodoAndPomodoroAddResponse save(TodoAndPomodoroAddRequest todoAndPomodoroAddRequest) {
        Todos todos = todoService.save(todoAndPomodoroAddRequest.getTodoAddRequest());
        Pomodoros pomodoros = pomodoroAddService.save(todoAndPomodoroAddRequest.getPomodoroAddRequest(), todos);

        TodoAddResponse todoAddResponse = new TodoAddResponse(todos);
        PomodoroAddResponse pomodoroAddResponse = new PomodoroAddResponse(pomodoros);
        return new TodoAndPomodoroAddResponse(todoAddResponse, pomodoroAddResponse);
    }
}
