package com.growth.task.pomodoro.service;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.dto.request.PomodoroAddRequest;
import com.growth.task.pomodoro.dto.request.PomodoroUpdateRequest;
import com.growth.task.pomodoro.dto.response.PomodoroUpdateResponse;
import com.growth.task.pomodoro.exception.PomodoroNotFoundException;
import com.growth.task.pomodoro.repository.PomodorosRepository;
import com.growth.task.todo.domain.Todos;
import com.growth.task.todo.exception.TodoNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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
        Pomodoros pomodoros = findPomodoroByTodoId(todoId);
        pomodoros.getTodo().validateUpdatePomodoro();
        pomodoros.updatePlanCount(pomodoroUpdateRequest.getPlanCount());

        pomodorosRepository.save(pomodoros);
        return pomodoros;
    }

    @Transactional
    public PomodoroUpdateResponse complete(Long todoId) {
        Pomodoros pomodoro = findPomodoroByTodoId(todoId);

        pomodoro.getTodo().validateCompletePomodoro();
        pomodoro.increasePerformCount();
        return new PomodoroUpdateResponse(pomodoro);
    }

    @Transactional(readOnly = true)
    private Pomodoros findPomodoroByTodoId(Long todoId) {
        return pomodorosRepository.findByTodo_TodoId(todoId)
                .orElseThrow(() -> new PomodoroNotFoundException());
    }
}
