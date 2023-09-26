package com.growth.task.pomodoro.exception;

public class PomodoroCountMismatchException extends RuntimeException {
    public PomodoroCountMismatchException() {
        super("Pomodoro Count mismatch.");
    }
}
