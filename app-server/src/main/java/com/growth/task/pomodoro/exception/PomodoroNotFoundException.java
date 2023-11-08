package com.growth.task.pomodoro.exception;


import com.growth.task.commons.advice.EntityNotFoundException;

public class PomodoroNotFoundException extends EntityNotFoundException {
    public PomodoroNotFoundException() {
        super("Pomodoro Not Found Exception");
    }
}
