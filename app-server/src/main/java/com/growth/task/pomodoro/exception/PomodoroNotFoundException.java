package com.growth.task.pomodoro.exception;


import com.growth.task.commons.error.exception.EntityNotFoundException;

public class PomodoroNotFoundException extends EntityNotFoundException {
    public PomodoroNotFoundException() {
        super("Pomodoro Not Found Exception");
    }
}
