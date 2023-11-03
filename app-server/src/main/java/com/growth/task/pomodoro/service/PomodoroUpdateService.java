package com.growth.task.pomodoro.service;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.dto.request.PomodoroUpdateRequest;
import com.growth.task.pomodoro.dto.response.PomodoroUpdateResponse;
import com.growth.task.pomodoro.exception.PomodoroNotFoundException;
import com.growth.task.pomodoro.repository.PomodorosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PomodoroUpdateService {
    private final PomodorosRepository pomodorosRepository;

    public PomodoroUpdateService(PomodorosRepository pomodorosRepository) {
        this.pomodorosRepository = pomodorosRepository;
    }

    @Transactional
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
