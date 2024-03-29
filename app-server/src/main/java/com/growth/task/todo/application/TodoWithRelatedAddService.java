package com.growth.task.todo.application;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.dto.response.PomodoroAddResponse;
import com.growth.task.pomodoro.service.PomodoroAddService;
import com.growth.task.todo.domain.TodoCategory;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.composite.TodoAndPomodoroAddRequest;
import com.growth.task.todo.dto.composite.TodoAndPomodoroAddResponse;
import com.growth.task.todo.dto.response.TodoAddResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoWithRelatedAddService {

    private final TodoAddService todoAddService;
    private final PomodoroAddService pomodoroAddService;
    private final TodoCategoryAddService todoCategoryAddService;

    public TodoWithRelatedAddService(
            TodoAddService todoAddService,
            PomodoroAddService pomodoroAddService,
            TodoCategoryAddService todoCategoryAddService) {
        this.todoAddService = todoAddService;
        this.pomodoroAddService = pomodoroAddService;
        this.todoCategoryAddService = todoCategoryAddService;
    }

    @Transactional
    public TodoAndPomodoroAddResponse saveWithRelated(TodoAndPomodoroAddRequest todoAndPomodoroAddRequest) {
        Todos todos = todoAddService.save(todoAndPomodoroAddRequest.getTodoAddRequest());
        Pomodoros pomodoros = pomodoroAddService.save(todoAndPomodoroAddRequest.getPomodoroAddRequest(), todos);
        TodoCategory todoCategory = todoCategoryAddService.save(todos, todoAndPomodoroAddRequest.getCategoryId());

        TodoAddResponse todoAddResponse = new TodoAddResponse(todos);
        PomodoroAddResponse pomodoroAddResponse = new PomodoroAddResponse(pomodoros);

        return new TodoAndPomodoroAddResponse(todoAddResponse, pomodoroAddResponse, todoCategory);
    }
}
