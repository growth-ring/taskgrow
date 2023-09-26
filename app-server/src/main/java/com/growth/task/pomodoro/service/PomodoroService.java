package com.growth.task.pomodoro.service;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.exception.PomodoroExceedPlanCountException;
import com.growth.task.pomodoro.repository.PomodorosRepository;
import com.growth.task.pomodoro.dto.request.PomodoroAddRequest;
import com.growth.task.pomodoro.dto.request.PomodoroUpdateRequest;
import com.growth.task.todo.domain.Todos;
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

    public Pomodoros update(Long todoId, PomodoroUpdateRequest pomodoroUpdateRequest) {
        Pomodoros pomodoros = pomodorosRepository.findByTodo_TodoId(todoId);
        if (pomodoroUpdateRequest != null) {
            pomodoros.updatePlanCount(pomodoroUpdateRequest.getPlanCount());
        }
        pomodorosRepository.save(pomodoros);
        return pomodoros;
    }

    public Pomodoros complete(Long todoId) {
        Pomodoros pomodoros = pomodorosRepository.findByTodo_TodoId(todoId);

        if (pomodoros.getPerformCount() >= pomodoros.getPlanCount()) {
            throw new PomodoroExceedPlanCountException();
        }
        pomodoros.updatePerformCount(pomodoros.getPerformCount() + 1);

        pomodorosRepository.save(pomodoros);
        return pomodoros;
    }
}
