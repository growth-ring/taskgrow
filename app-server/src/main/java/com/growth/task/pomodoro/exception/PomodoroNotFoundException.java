package com.growth.task.pomodoro.exception;

import jakarta.persistence.EntityNotFoundException;

public class PomodoroNotFoundException extends EntityNotFoundException {
    public PomodoroNotFoundException() {
        super("Pomodoro Not Found Exception");
    }
}
