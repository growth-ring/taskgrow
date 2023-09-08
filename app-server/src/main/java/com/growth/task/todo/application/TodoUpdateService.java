package com.growth.task.todo.application;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.dto.response.PomodoroUpdateResponse;
import com.growth.task.pomodoro.service.PomodoroService;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.composite.TodoAndPomodoroUpdateRequest;
import com.growth.task.todo.dto.composite.TodoAndPomodoroUpdateResponse;
import com.growth.task.todo.dto.response.TodoUpdateResponse;
import org.springframework.stereotype.Service;

@Service
public class TodoUpdateService {

    private final TodoService todoService;
    private final PomodoroService pomodoroService;
    
    public TodoUpdateService(
            TodoService todoService,
            PomodoroService pomodoroService
    ) {
        this.todoService = todoService;
        this.pomodoroService = pomodoroService;
    }

    public TodoAndPomodoroUpdateResponse update(Long todoId, TodoAndPomodoroUpdateRequest todoAndPomodoroUpdateRequest) {
        Todos todos = todoService.update(todoId, todoAndPomodoroUpdateRequest);
        Pomodoros pomodoros = pomodoroService.update(todoId, todoAndPomodoroUpdateRequest);

        TodoUpdateResponse todoUpdateResponse = new TodoUpdateResponse(todos);
        PomodoroUpdateResponse pomodoroUpdateResponse = new PomodoroUpdateResponse(pomodoros);
        return new TodoAndPomodoroUpdateResponse(todoUpdateResponse, pomodoroUpdateResponse);
    }
}
