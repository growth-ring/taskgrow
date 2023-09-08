package com.growth.task.pomodoro.service;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.repository.PomodorosRepository;
import com.growth.task.pomodoro.dto.request.PomodoroAddRequest;
import com.growth.task.pomodoro.dto.request.PomodoroUpdateRequest;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.dto.composite.TodoAndPomodoroUpdateRequest;
import com.growth.task.todo.exception.TodoNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PomodoroService {
    private final PomodorosRepository pomodorosRepository;

    public PomodoroService(PomodorosRepository pomodorosRepository) {
        this.pomodorosRepository = pomodorosRepository;
    }

    public Pomodoros save(PomodoroAddRequest pomodoroAddRequest, Todos todos) {
        if (todos.getTodoId() == null) {
            throw new TodoNotFoundException();
        }
        Pomodoros pomodoros = pomodoroAddRequest.toEntity(todos);
        return pomodorosRepository.save(pomodoros);
    }

    public Pomodoros update(Long todoId, TodoAndPomodoroUpdateRequest todoAndPomodoroUpdateRequest) {
        Pomodoros pomodoros = pomodorosRepository.findByTodo_TodoId(todoId);
        PomodoroUpdateRequest pomodoroUpdateReq = todoAndPomodoroUpdateRequest.getPomodoroUpdateRequest();
        if(pomodoroUpdateReq != null) {
            pomodoros.updatePerformCount(pomodoroUpdateReq.getPerformCount());
            pomodoros.updatePlanCount(pomodoroUpdateReq.getPlanCount());
        }
        pomodorosRepository.save(pomodoros);
        return pomodoros;
    }
}
