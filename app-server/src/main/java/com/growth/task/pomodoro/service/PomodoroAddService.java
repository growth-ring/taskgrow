package com.growth.task.pomodoro.service;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.dto.request.PomodoroAddRequest;
import com.growth.task.pomodoro.repository.PomodorosRepository;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.exception.TodoNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PomodoroAddService {
    private final PomodorosRepository pomodorosRepository;

    public PomodoroAddService(PomodorosRepository pomodorosRepository) {
        this.pomodorosRepository = pomodorosRepository;
    }

    @Transactional
    public Pomodoros save(PomodoroAddRequest pomodoroAddRequest, Todos todos) {
        if (todos.getTodoId() == null) {
            throw new TodoNotFoundException();
        }
        Pomodoros pomodoros = pomodoroAddRequest.toEntity(todos);
        return pomodorosRepository.save(pomodoros);
    }
}
