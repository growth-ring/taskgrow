package com.growth.task.pomodoro.service;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.domain.PomodorosRepository;
import com.growth.task.pomodoro.dto.request.PomodoroAddRequest;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.exception.TaskNotFoundException;
import com.growth.task.todo.exception.TodoNotFoundException;
import com.growth.task.todo.repository.TodosRepository;
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
}
