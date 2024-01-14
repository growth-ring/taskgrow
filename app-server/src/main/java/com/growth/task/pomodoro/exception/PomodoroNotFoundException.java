package com.growth.task.pomodoro.exception;


import com.growth.task.global.error.exception.EntityNotFoundException;

public class PomodoroNotFoundException extends EntityNotFoundException {
    public PomodoroNotFoundException() {
        super("Pomodoro Not Found Exception");
    }
}
