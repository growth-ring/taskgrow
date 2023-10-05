package com.growth.task.pomodoro.controller;

import com.growth.task.pomodoro.domain.Pomodoros;
import com.growth.task.pomodoro.dto.request.PomodoroUpdateRequest;
import com.growth.task.pomodoro.dto.response.PomodoroUpdateResponse;
import com.growth.task.pomodoro.service.PomodoroAddService;
import com.growth.task.pomodoro.service.PomodoroUpdateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/pomodoros")
@Tag(name = "Pomodoro", description = "Pomodoro API Document")
public class PomodoroUpdateController {

    private final PomodoroUpdateService pomodoroUpdateService;

    public PomodoroUpdateController(PomodoroUpdateService pomodoroUpdateService) {
        this.pomodoroUpdateService = pomodoroUpdateService;
    }

    @PatchMapping("/{todo_id}")
    @ResponseStatus(OK)
    public PomodoroUpdateResponse update(
            @PathVariable("todo_id") Long todoId,
            @RequestBody @Valid PomodoroUpdateRequest pomodoroUpdateRequest
    ) {
        Pomodoros pomodoros = pomodoroUpdateService.update(todoId, pomodoroUpdateRequest);
        return new PomodoroUpdateResponse(pomodoros);
    }

    @PatchMapping("/{todo_id}/complete")
    public ResponseEntity<PomodoroUpdateResponse> complete(
            @PathVariable("todo_id") Long todoId
    ) {
        PomodoroUpdateResponse pomodoro = pomodoroUpdateService.complete(todoId);
        return ResponseEntity.ok(pomodoro);
    }
}
